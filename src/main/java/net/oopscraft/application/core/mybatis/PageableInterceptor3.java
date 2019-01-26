package net.oopscraft.application.core.mybatis;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

import org.apache.ibatis.binding.MapperMethod.ParamMap;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.CachingExecutor;
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
	//,@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class,Integer.class})
})
public class PageableInterceptor3 implements Interceptor {
	
	enum DatabaseId { ANSI, ORACLE, MYSQL, POSTRES, MSSQL }
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PageableInterceptor3.class);
	private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
	private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();
	private static final ReflectorFactory DEFAULT_REFLECTOR_FACTORY = new DefaultReflectorFactory();
	
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		
		Object[] args = invocation.getArgs();
		
		

		LOGGER.warn("############ Executor {}", Arrays.toString(args));
		Executor executor = (Executor) invocation.getTarget();
		MappedStatement mappedStatement = (MappedStatement)args[0];
		LOGGER.warn("mappedStatement: {}" + mappedStatement);
		Object parameterObject = args[1];
		LOGGER.warn("parameterObject: {}" + parameterObject);
		RowBounds rowBounds = (RowBounds) args[2];
		LOGGER.warn("rowBounds: {}" + rowBounds);
		
		Configuration configuration = mappedStatement.getConfiguration();
		
		BoundSql boundSql = mappedStatement.getBoundSql(parameterObject);
		LOGGER.warn("boundSql: {}" + boundSql);

		List<ParameterMapping> parameterMappings = mappedStatement.getParameterMap().getParameterMappings();
		LOGGER.warn("parameterMappings: {}" + parameterMappings);
		
		
		mappedStatement.getCache().clear();
		if(executor instanceof CachingExecutor) {
			CachingExecutor cachingExecutor = (CachingExecutor)executor;
			CacheKey cacheKey = cachingExecutor.createCacheKey(mappedStatement, parameterObject, rowBounds, boundSql);
			//LOGGER.warn("cacheKey:{}", cacheKey);
		}
		
		List<ParameterMapping> pageableParameterMapping = new ArrayList<ParameterMapping>();
		for(ParameterMapping parameterMapping : parameterMappings){
			pageableParameterMapping.add(parameterMapping);
		}
		
		StringBuffer pageableSql = new StringBuffer();
		pageableSql.append("SELECT _dat.* FROM (");
		pageableSql.append(boundSql.getSql());
		pageableSql.append(") _dat ");
		pageableSql.append(" LIMIT ? ");
		pageableSql.append(" OFFSET ? ");
		pageableParameterMapping.add(new ParameterMapping.Builder(configuration, "pageableRowBounds.sqlLimit", Integer.class).build());
		pageableParameterMapping.add(new ParameterMapping.Builder(configuration, "pageableRowBounds.sqlOffset", Integer.class).build());
		
		
		MetaObject metaObject = MetaObject.forObject(MappedStatement.class, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_REFLECTOR_FACTORY);
//		
//		mappedStatement.get
//		metaObject.setValue("delegate.boundSql.sql", pageableSql.toString());
		
		return invocation.proceed();

		
		
		
		
		
		
//		LOGGER.warn("+ PageableInterceptor.intercept");
//		LOGGER.warn("invocation:{}", invocation);
//		StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
//		LOGGER.warn("statementHandler:{}", statementHandler);
//		MetaObject metaObject = MetaObject.forObject(statementHandler, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_REFLECTOR_FACTORY);
//		
//		PageableRowBounds pageableRowBounds = null;
//		try {
//			pageableRowBounds = (PageableRowBounds)metaObject.getValue("delegate.rowBounds");
//		}catch(Exception e) {
//			LOGGER.debug("RowBounds not found. skip.");
//			return invocation.proceed();
//		}
//		
//		Configuration configuration = (Configuration) metaObject.getValue("delegate.configuration");
//		DatabaseId databaseId = null;
//		try {
//			databaseId = DatabaseId.valueOf(configuration.getDatabaseId());
//		}catch(Exception e) {
//			LOGGER.warn("Unknown Database ID:{}", configuration.getDatabaseId());
//			databaseId = DatabaseId.ANSI;
//		}
//		
//		// convert 
//		convertPageable(metaObject, pageableRowBounds, configuration, databaseId);
//		
//		if(pageableRowBounds.isEnableTotalCount()) {
//			this.setTotalCount(metaObject, pageableRowBounds, configuration, databaseId);
//		}
//		
//		// default
//		return invocation.proceed();
	}
	
	/**
	 * convert pageable
	 * @param originalSql
	 * @return
	 */
	private static void convertPageable(MetaObject metaObject, final PageableRowBounds pageableRowBounds, Configuration configuration, DatabaseId databaseId) {

		String originalSql = (String) metaObject.getValue("delegate.boundSql.sql");
		
		@SuppressWarnings("unchecked")
		ParamMap<PageableRowBounds> parameterObject = (ParamMap<PageableRowBounds>) metaObject.getValue("delegate.boundSql.parameterObject");
		if(parameterObject == null) {
			parameterObject = new ParamMap<PageableRowBounds>();
		}
		parameterObject.put("pageableRowBounds", pageableRowBounds);
		
		@SuppressWarnings("unchecked")
		List<ParameterMapping> originalParameterMappings = (List<ParameterMapping>) metaObject.getValue("delegate.boundSql.parameterMappings");
		List<ParameterMapping> parameterMappings = new ArrayList<ParameterMapping>();
		for(ParameterMapping parameterMapping : originalParameterMappings){
			parameterMappings.add(parameterMapping);
		}

		// defines prefix SQL
		StringBuffer pageableSql = new StringBuffer();
		switch(databaseId) {
			case ORACLE :
				pageableSql.append("SELECT * FROM (SELECT ROWNUM AS i,dat.* FROM (");
				pageableSql.append(originalSql);
				pageableSql.append(") dat) WHERE i BETWEEN (?+1) AND (?+?)");
				parameterMappings.add(new ParameterMapping.Builder(configuration, "pageableRowBounds.sqlOffset", Integer.class).build());
				parameterMappings.add(new ParameterMapping.Builder(configuration, "pageableRowBounds.sqlOffset", Integer.class).build());
				parameterMappings.add(new ParameterMapping.Builder(configuration, "pageableRowBounds.sqlLimit", Integer.class).build());
			break;
			case MYSQL :
				pageableSql.append("SELECT _dat.* FROM (");
				pageableSql.append(originalSql);
				pageableSql.append(") _dat ");
				pageableSql.append(" LIMIT ? ");
				pageableSql.append(" OFFSET ? ");
				parameterMappings.add(new ParameterMapping.Builder(configuration, "pageableRowBounds.sqlLimit", Integer.class).build());
				parameterMappings.add(new ParameterMapping.Builder(configuration, "pageableRowBounds.sqlOffset", Integer.class).build());
			break;
			case MSSQL :
				pageableSql.append(originalSql);
				Pattern pattern = Pattern.compile("(?i)ORDER[\\s]{1,}(?i)BY[\\s]{1,}(.*)");
				if(pattern.matcher(originalSql).find() == false) {
					pageableSql.append(" ORDER BY 1 ");
				}
				pageableSql.append(" OFFSET ? ROWS ");
				pageableSql.append(" FETCH NEXT ? ROWS ONLY ");
				parameterMappings.add(new ParameterMapping.Builder(configuration, "pageableRowBounds.sqlOffset", Integer.class).build());
				parameterMappings.add(new ParameterMapping.Builder(configuration, "pageableRowBounds.sqlLimit", Integer.class).build());
			break;
			// LIMIT OFFSET 지원하지 않거나 특정패턴의 SQL핸들링으로 처리가 힘든 케이스는 null 반환
			// (null일 경우 기본 RowBounds 기능으로 페이징처리)
			default :
				return;
		}
		metaObject.setValue("delegate.boundSql.sql", pageableSql.toString());
		metaObject.setValue("delegate.boundSql.parameterMappings", parameterMappings);
		pageableRowBounds.setOffset(0);
	}
	
	/**
	 * Getting total count
	 * @param metaObject
	 * @param pageableRowBounds
	 * @throws SQLException
	 */
	private void setTotalCount(MetaObject metaObject, final PageableRowBounds pageableRowBounds, Configuration configuration, DatabaseId databaseId) throws SQLException {
		
		String originalSql = (String) metaObject.getValue("delegate.boundSql.sql");
		
		// count SQL
		StringBuffer totalCountSql = createTotalCountSql(originalSql, databaseId);
		
		@SuppressWarnings("unchecked")
		List<ParameterMapping> parameterMappings = (List<ParameterMapping>) metaObject.getValue("delegate.boundSql.parameterMappings");
		@SuppressWarnings("unchecked")
		ParamMap<PageableRowBounds> paramMap = (ParamMap<PageableRowBounds>)metaObject.getValue("delegate.boundSql.parameterObject");
		MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
		final BoundSql totalCountBoundSql = new BoundSql(configuration, totalCountSql.toString(), parameterMappings, paramMap);
		@SuppressWarnings("unchecked")
		Map<String,Object> additionalParameters = (Map<String, Object>) metaObject.getValue("delegate.boundSql.additionalParameters");
		for(String name : additionalParameters.keySet()) {
			totalCountBoundSql.setAdditionalParameter(name, additionalParameters.get(name));
		}
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
		Executor executor = (Executor) metaObject.getValue("delegate.executor");
		pageableRowBounds.setTotalCount(0);
		executor.query(totalCountMappedStatement, paramMap, RowBounds.DEFAULT, new ResultHandler<Integer>() {
			@Override
			public void handleResult(ResultContext<? extends Integer> resultContext) {
				Integer totalCount = resultContext.getResultObject();
				LOGGER.info("+ totalCount:" + totalCount);
				pageableRowBounds.setTotalCount(totalCount);
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
		LOGGER.debug("PageableInterceptor.plugin");
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
		LOGGER.debug("PageableInterceptor.setProperties");
	}
	
}
