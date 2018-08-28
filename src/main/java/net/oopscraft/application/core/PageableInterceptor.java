package net.lotte.chamomile.batch.core;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

import net.lotte.chamomile.batch.core.DatabaseIdProvider.DatabaseId;

@Intercepts({
	@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class,Integer.class})
})
public class PageableInterceptor implements Interceptor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PageableInterceptor.class);
	private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
	private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();
	private static final ReflectorFactory DEFAULT_REFLECTOR_FACTORY = new DefaultReflectorFactory();
	
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		LOGGER.debug("+ PageableInterceptor.intercept");
		StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
		MetaObject metaStatementHandler = MetaObject.forObject(statementHandler, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_REFLECTOR_FACTORY);
		RowBounds rowBounds = (RowBounds)metaStatementHandler.getValue("delegate.rowBounds");
		
		if(rowBounds != null && rowBounds instanceof Pageable) {
	
			Pageable pageable = (Pageable)rowBounds;
			
			//setting database id
			String databaseIdString = (String)metaStatementHandler.getValue("delegate.configuration.databaseId");
			DatabaseId databaseId = DatabaseId.valueOf(databaseIdString.toUpperCase());
			
			// totalCount
			if(pageable.enableTotalCount() == true) {
				setTotalCount(metaStatementHandler, pageable, databaseId);
			}

			// convert page query
			convertPageableSql(metaStatementHandler, pageable, databaseId);

			// proceed
			return invocation.proceed();
		}
		
		// default
		return invocation.proceed();
	}

	/**
	 * convert sql
	 * @param originalSql
	 * @return
	 */
	private static void convertPageableSql(MetaObject metaStatementHandler, final Pageable pageable, DatabaseId databaseId) {
		
		Configuration configuration = (Configuration) metaStatementHandler.getValue("delegate.configuration");
		String originalSql = (String) metaStatementHandler.getValue("delegate.boundSql.sql");
		@SuppressWarnings("unchecked")
		ParamMap<Pageable> paramMap = (ParamMap<Pageable>)metaStatementHandler.getValue("delegate.boundSql.parameterObject");
		paramMap.put("pageable", pageable);
		
		@SuppressWarnings("unchecked")
		List<ParameterMapping> originalParameterMappings = (List<ParameterMapping>) metaStatementHandler.getValue("delegate.boundSql.parameterMappings");
		List<ParameterMapping> parameterMappings = new ArrayList<ParameterMapping>();
		for(ParameterMapping parameterMapping : originalParameterMappings){
			parameterMappings.add(parameterMapping);
		}

		// defines prefix sql
		StringBuffer pageableSql = new StringBuffer();
		switch(databaseId) {
			case ORACLE :
				pageableSql.append("SELECT * FROM (SELECT ROWNUM AS i,dat.* FROM (");
				pageableSql.append(originalSql);
				pageableSql.append(") dat) WHERE i BETWEEN (?+1) AND (?+?)");
				parameterMappings.add(new ParameterMapping.Builder(configuration, "pageable.sqlOffset", Integer.class).build());
				parameterMappings.add(new ParameterMapping.Builder(configuration, "pageable.sqlOffset", Integer.class).build());
				parameterMappings.add(new ParameterMapping.Builder(configuration, "pageable.sqlLimit", Integer.class).build());
			break;
			case MYSQL :
				pageableSql.append("SELECT _dat.* FROM (");
				pageableSql.append(originalSql);
				pageableSql.append(") _dat ");
				pageableSql.append(" LIMIT ? ");
				pageableSql.append(" OFFSET ? ");
				parameterMappings.add(new ParameterMapping.Builder(configuration, "pageable.sqlLimit", Integer.class).build());
				parameterMappings.add(new ParameterMapping.Builder(configuration, "pageable.sqlOffset", Integer.class).build());
			break;
			case MSSQL :
				pageableSql.append(originalSql);
				Pattern pattern = Pattern.compile("(?i)ORDER[\\s]{1,}(?i)BY[\\s]{1,}(.*)");
				if(pattern.matcher(originalSql).find() == false) {
					pageableSql.append(" ORDER BY 1 ");
				}
				pageableSql.append(" OFFSET ? ROWS ");
				pageableSql.append(" FETCH NEXT ? ROWS ONLY ");
				parameterMappings.add(new ParameterMapping.Builder(configuration, "pageable.sqlOffset", Integer.class).build());
				parameterMappings.add(new ParameterMapping.Builder(configuration, "pageable.sqlLimit", Integer.class).build());
			break;
			// LIMIT OFFSET 지원하지 않거나 특정패턴의 SQL핸들링으로 처리가 힘든 케이스는 null 반환
			// (null일 경우 기본 RowBounds 기능으로 페이징처리)
			default :
				pageableSql.append(originalSql);
				return;
		}
		metaStatementHandler.setValue("delegate.boundSql.sql", pageableSql.toString());
		metaStatementHandler.setValue("delegate.boundSql.parameterMappings", parameterMappings);
		// sql에서 페이징 처리되므로 실제 offset, limit는  default값으로 변경
		pageable.setOffset(0);
		pageable.setLimit(pageable.getRows());
	}
	
	/**
	 * Getting total count
	 * @param metaStatementHandler
	 * @param pageable
	 * @throws SQLException
	 */
	private void setTotalCount(MetaObject metaStatementHandler, final Pageable pageable, DatabaseId databaseId) throws SQLException {
		Configuration configuration = (Configuration) metaStatementHandler.getValue("delegate.configuration");
		String originalSql = (String) metaStatementHandler.getValue("delegate.boundSql.sql");
		
		// count sql
		StringBuffer totalCountSql = createTotalCountSql(originalSql, databaseId);
		
		@SuppressWarnings("unchecked")
		List<ParameterMapping> parameterMappings = (List<ParameterMapping>) metaStatementHandler.getValue("delegate.boundSql.parameterMappings");
		@SuppressWarnings("unchecked")
		ParamMap<Pageable> paramMap = (ParamMap<Pageable>)metaStatementHandler.getValue("delegate.boundSql.parameterObject");
		MappedStatement mappedStatement = (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");
		final BoundSql totalCountBoundSql = new BoundSql(configuration, totalCountSql.toString(), parameterMappings, paramMap);
		@SuppressWarnings("unchecked")
		Map<String,Object> additionalParameters = (Map<String, Object>) metaStatementHandler.getValue("delegate.boundSql.additionalParameters");
		for(String name : additionalParameters.keySet()) {
			totalCountBoundSql.setAdditionalParameter(name, additionalParameters.get(name));
		}
		SqlSource totalCountSqlSource = new SqlSource() {
			@Override
			public BoundSql getBoundSql(Object parameterObject) {
				return totalCountBoundSql;
			}
		};

		MappedStatement.Builder builder = new MappedStatement.Builder(configuration, mappedStatement.getId()+"_cnt", totalCountSqlSource, mappedStatement.getSqlCommandType());
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
		Executor executor = (Executor) metaStatementHandler.getValue("delegate.executor");
		pageable.setTotalCount(0);
		executor.query(totalCountMappedStatement, paramMap, RowBounds.DEFAULT, new ResultHandler<Integer>() {
			@Override
			public void handleResult(ResultContext<? extends Integer> resultContext) {
				Integer totalCount = resultContext.getResultObject();
				LOGGER.debug("+ totalCount:" + totalCount);
				pageable.setTotalCount(totalCount);
			}
		});
	}
	
	/**
	 * Creates total count query
	 * @param originalSql
	 * @return
	 */
	protected static StringBuffer createTotalCountSql(String originalSql, DatabaseId databaseId) {
		StringBuffer totalCountSql = new StringBuffer();
		
		// defines prefix sql
		switch(databaseId) {
			// MSSQL은  In-line View처리 시 ORDER BY절이 존재하는 경우 오류 발생함.
			case MSSQL :
				totalCountSql.append("SELECT COUNT(*) FROM (");
				totalCountSql.append(originalSql);
				totalCountSql.append(") DAT");
				String fixedTotalCountSql = totalCountSql.toString().replaceAll("(?i)ORDER[\\s]{1,}(?i)BY[\\s]{1,}(.*)\\) DAT", ") DAT");
				totalCountSql.setLength(0);
				totalCountSql.append(fixedTotalCountSql);
			break;
			// ANSI지원하지 않는 DBMS 제외하고는 모두  ANSI로 통일
			default :
				totalCountSql.append("SELECT COUNT(*) FROM (");
				totalCountSql.append(originalSql);
				totalCountSql.append(") DAT");
			break;
		}
		return totalCountSql;
	}

	@Override
	public Object plugin(Object target) {
		LOGGER.debug("+ PageableInterceptor.plugin");
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
		LOGGER.debug("+ PageableInterceptor.setProperties");
	}
	
}
