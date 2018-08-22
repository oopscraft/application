package net.oopscraft.application.core;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

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

import net.oopscraft.application.core.DatabaseIdProvider.DatabaseId;

@Intercepts({
	@Signature(type = StatementHandler.class, method = "parameterize", args = {Statement.class})
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
			@SuppressWarnings("unchecked")
			ParamMap<Pageable> paramMap = (ParamMap<Pageable>)metaStatementHandler.getValue("delegate.boundSql.parameterObject");
			paramMap.put("pageable", pageable);
			String originalSql = (String) metaStatementHandler.getValue("delegate.boundSql.sql");
			LOGGER.debug("originalSql = {}", originalSql);
			LOGGER.debug("pageable = {}", pageable);
			
			//setting database id
			String databaseIdString = (String)metaStatementHandler.getValue("delegate.configuration.databaseId");
			DatabaseId databaseId = DatabaseId.valueOf(databaseIdString.toUpperCase());
			
			// totalCount
			if(pageable.enableTotalRows() == true) {
				setTotalRows(metaStatementHandler, pageable);
			}

			// pageable
			StringBuffer sql = this.createPageableSql(originalSql, databaseId);
			LOGGER.debug("sql = {}", sql.toString());
			metaStatementHandler.setValue("delegate.boundSql.sql", sql.toString());

			return invocation.proceed();
		}
		
		// default
		return invocation.proceed();
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
	
	/**
	 * convert sql
	 * @param originalSql
	 * @return
	 */
	private StringBuffer createPageableSql(String originalSql, DatabaseId databaseId) {
		
		StringBuffer pageableSql = new StringBuffer();
		
		// defines prefix SQL
		switch(databaseId) {
			case ORACLE :
				pageableSql.append("SELECT * FROM (SELECT ROWNUM AS i,DAT.* FROM (");
				pageableSql.append(originalSql);
				pageableSql.append(") DAT) WHERE i BETWEEN (#{pageable.offset}+1) AND (#{pageable.offset}+#{pageable.limit})");
			break;
			default :
				pageableSql.append("SELECT * FROM (");
				pageableSql.append(originalSql);
				pageableSql.append(") LIMIT #{pageable.limit}");
				pageableSql.append(" OFFSET #{pageable.offset}");
			break;
		}

		return pageableSql;
	}
	
	/**
	 * Getting total count
	 * @param metaStatementHandler
	 * @param pageable
	 * @throws SQLException
	 */
	private void setTotalRows(MetaObject metaStatementHandler, final Pageable pageable) throws SQLException {
		Configuration configuration = (Configuration) metaStatementHandler.getValue("delegate.configuration");
		String originalSql = (String) metaStatementHandler.getValue("delegate.boundSql.sql");
		
		// count sql
		StringBuffer totalCountSql = createTotalRowsSql(originalSql);
		
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
		pageable.setTotalRows(0);
		executor.query(totalCountMappedStatement, paramMap, RowBounds.DEFAULT, new ResultHandler<Integer>() {
			@Override
			public void handleResult(ResultContext<? extends Integer> resultContext) {
				Integer totalRows = resultContext.getResultObject();
				LOGGER.debug("+ totalCount:" + totalRows);
				pageable.setTotalRows(totalRows);
			}
		});
	}
	
	/**
	 * Creates total count query
	 * @param originalSql
	 * @return
	 */
	private static StringBuffer createTotalRowsSql(String originalSql) {
		StringBuffer totalRowsSql = new StringBuffer();
		totalRowsSql.append("SELECT COUNT(*) FROM (");
		totalRowsSql.append(originalSql);
		totalRowsSql.append(") DAT");
		return totalRowsSql;
	}

}
