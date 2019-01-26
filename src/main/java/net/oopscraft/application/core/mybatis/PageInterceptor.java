package net.oopscraft.application.core.mybatis;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import org.apache.ibatis.binding.MapperMethod.ParamMap;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Intercepts({
	  @Signature(type = Executor.class,  method = "query", args = {MappedStatement.class, Object.class, RowBounds.class,ResultHandler.class})
	 ,@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})
})
public class PageInterceptor implements Interceptor, Serializable {
	
	private static final long serialVersionUID = 2332692969371928141L;

	enum DatabaseId { ANSI, ORACLE, MYSQL, POSTRES, MSSQL }
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PageInterceptor.class);
	private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
	private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();
	private static final ReflectorFactory DEFAULT_REFLECTOR_FACTORY = new DefaultReflectorFactory();
	
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Object target = invocation.getTarget();
		Method method = invocation.getMethod();
		Object[] args = invocation.getArgs();
		LOGGER.trace("invocation - target:{}, method:{}", target, method, Arrays.toString(args));
		
		if(target instanceof Executor && "query".equals(method.getName())) {
			setTotalCount(invocation);
		} else if(target instanceof StatementHandler && "prepare".equals(method.getName())) {
			convertPageable(invocation);
		}
		
		return invocation.proceed();
	}
	
	/**
	 * setTotalCount
	 * @param invocation
	 * @throws Exception
	 */
	private static void setTotalCount(Invocation invocation) throws Exception {
		Object target = invocation.getTarget();
		Method method = invocation.getMethod();
		Object[] args = invocation.getArgs();
		LOGGER.info("invocation - target:{}, method:{}", target, method);
		
		// checks pageRowBounds
		final PageRowBounds pageRowBounds;
		try {
			pageRowBounds = (PageRowBounds)args[2];
		}catch(Exception e) {
			return;
		}
		if(pageRowBounds.getTotalCount() > -1) {
			return;
		}
		
		Executor executor = (Executor)target;
		MappedStatement mappedStatement = (MappedStatement)args[0];
		@SuppressWarnings("unchecked")
		ParamMap<PageRowBounds> parameterObject = (ParamMap<PageRowBounds>)args[1];
		
		Configuration configuration = mappedStatement.getConfiguration();
		BoundSql boundSql = mappedStatement.getBoundSql(parameterObject);
		
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		
		// defines prefix sql
		String sql = boundSql.getSql();
		DatabaseId databaseId = DatabaseId.valueOf(configuration.getDatabaseId());
		StringBuffer totalCountSql = new StringBuffer();
		switch(databaseId) {
			// MSSQL은  In-line View처리 시 ORDER BY절이 존재하는 경우 오류 발생함.
			case MSSQL :
				totalCountSql.append("SELECT COUNT(*) FROM (");
				totalCountSql.append(sql);
				totalCountSql.append(") DAT");
				String fixedTotalCountSql = totalCountSql.toString().replaceAll("(?i)ORDER[\\s]{1,}(?i)BY[\\s]{1,}(.*)\\) DAT", ") DAT");
				totalCountSql.setLength(0);
				totalCountSql.append(fixedTotalCountSql);
			break;
			// ANSI지원하지 않는 DBMS 제외하고는 모두  ANSI로 통일
			default :
				totalCountSql.append("SELECT COUNT(*) FROM (");
				totalCountSql.append(sql);
				totalCountSql.append(") DAT");
			break;
		}
		
		final BoundSql totalCountBoundSql = new BoundSql(configuration, totalCountSql.toString(), parameterMappings, parameterObject);
		SqlSource totalCountSqlSource = new SqlSource() {
			@Override
			public BoundSql getBoundSql(Object parameterObject) {
				return totalCountBoundSql;
			}
		};

		MappedStatement.Builder builder = new MappedStatement.Builder(configuration, mappedStatement.getId() + "_cnt", totalCountSqlSource, mappedStatement.getSqlCommandType());
		builder.resource(mappedStatement.getResource());
		builder.fetchSize(mappedStatement.getFetchSize());
		builder.statementType(mappedStatement.getStatementType());
		builder.keyGenerator(mappedStatement.getKeyGenerator());
		String[] keyProperties = mappedStatement.getKeyProperties();
		builder.keyProperty(keyProperties == null ? null : keyProperties[0]);
		builder.timeout(mappedStatement.getTimeout());
		builder.parameterMap(mappedStatement.getParameterMap());
		builder.cache(mappedStatement.getCache());
		builder.flushCacheRequired(mappedStatement.isFlushCacheRequired());
		builder.useCache(mappedStatement.isUseCache());

		List<ResultMapping> resultMappings = new ArrayList<ResultMapping>();
		ResultMap.Builder resultMapBuilder = new ResultMap.Builder(configuration, mappedStatement.getId() + "_cnt", Integer.class, resultMappings);
		ResultMap resultMap = resultMapBuilder.build();
		List<ResultMap> resultMaps = new ArrayList<ResultMap>();
		resultMaps.add(resultMap);
		builder.resultMaps(resultMaps);

		MappedStatement totalCountMappedStatement = builder.build();
		pageRowBounds.setTotalCount(0);
		
		executor.query(totalCountMappedStatement, parameterObject, RowBounds.DEFAULT, new ResultHandler<Integer>() {
			@Override
			public void handleResult(ResultContext<? extends Integer> resultContext) {
				Integer totalCount = resultContext.getResultObject();
				LOGGER.info("+ totalCount:" + totalCount);
				pageRowBounds.setTotalCount(totalCount);
			}
		});
		
		
		return;
	}
	
	/**
	 * convertPageable
	 * @param originalSql
	 * @return
	 */
	private static void convertPageable(Invocation invocation) {
		LOGGER.info("convertPageable[{}]", invocation);
		StatementHandler statementHandler = (StatementHandler)invocation.getTarget();
		MetaObject metaObject = MetaObject.forObject(statementHandler, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_REFLECTOR_FACTORY);

		// checks pageRowBounds
		PageRowBounds pageRowBounds = null;
		try {
			pageRowBounds = (PageRowBounds)metaObject.getValue("delegate.rowBounds");
		}catch(Exception e) {
			return;
		}

		Configuration configuration = (Configuration) metaObject.getValue("delegate.configuration");
		@SuppressWarnings("unchecked")
		ParamMap<PageRowBounds> parameterObject = (ParamMap<PageRowBounds>) metaObject.getValue("delegate.boundSql.parameterObject");
		@SuppressWarnings("unchecked")
		List<ParameterMapping> parameterMappings = (List<ParameterMapping>) metaObject.getValue("delegate.boundSql.parameterMappings");
		String sql = (String) metaObject.getValue("delegate.boundSql.sql");
		
		if(parameterObject == null) {
			parameterObject = new ParamMap<PageRowBounds>();
		}
		parameterObject.put("pageRowBounds", pageRowBounds);
		
		List<ParameterMapping> pageParameterMappings = new ArrayList<ParameterMapping>();
		for(ParameterMapping parameterMapping : parameterMappings){
			pageParameterMappings.add(parameterMapping);
		}
		
		// defines prefix SQL
		DatabaseId databaseId = DatabaseId.valueOf(configuration.getDatabaseId());
		StringBuffer pageSql = new StringBuffer();
		switch(databaseId) {
			case ORACLE :
				pageSql.append("SELECT * FROM (SELECT ROWNUM AS i,dat.* FROM (");
				pageSql.append(sql);
				pageSql.append(") dat) WHERE i BETWEEN (?+1) AND (?+?)");
				pageParameterMappings.add(new ParameterMapping.Builder(configuration, "pageRowBounds.sqlOffset", Integer.class).build());
				pageParameterMappings.add(new ParameterMapping.Builder(configuration, "pageRowBounds.sqlOffset", Integer.class).build());
				pageParameterMappings.add(new ParameterMapping.Builder(configuration, "pageRowBounds.sqlLimit", Integer.class).build());
			break;
			case MYSQL :
				pageSql.append("SELECT _dat.* FROM (");
				pageSql.append(sql);
				pageSql.append(") _dat ");
				pageSql.append(" LIMIT ? ");
				pageSql.append(" OFFSET ? ");
				pageParameterMappings.add(new ParameterMapping.Builder(configuration, "pageRowBounds.sqlLimit", Integer.class).build());
				pageParameterMappings.add(new ParameterMapping.Builder(configuration, "pageRowBounds.sqlOffset", Integer.class).build());
			break;
			case MSSQL :
				pageSql.append(sql);
				Pattern pattern = Pattern.compile("(?i)ORDER[\\s]{1,}(?i)BY[\\s]{1,}(.*)");
				if(pattern.matcher(sql).find() == false) {
					pageSql.append(" ORDER BY 1 ");
				}
				pageSql.append(" OFFSET ? ROWS ");
				pageSql.append(" FETCH NEXT ? ROWS ONLY ");
				pageParameterMappings.add(new ParameterMapping.Builder(configuration, "pageRowBounds.sqlOffset", Integer.class).build());
				pageParameterMappings.add(new ParameterMapping.Builder(configuration, "pageRowBounds.sqlLimit", Integer.class).build());
			break;
			// LIMIT OFFSET 지원하지 않거나 특정패턴의 SQL핸들링으로 처리가 힘든 케이스는 null 반환
			// (null일 경우 기본 RowBounds 기능으로 페이징처리)
			default :
				return;
		}
		metaObject.setValue("delegate.boundSql.sql", pageSql.toString());
		metaObject.setValue("delegate.boundSql.parameterMappings", pageParameterMappings);
		pageRowBounds.setOffset(0);
	}
	
	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
		// void
	}
	
}
