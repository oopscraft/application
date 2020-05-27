package net.oopscraft.application;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.cache.spi.CachingProvider;
import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.apache.commons.text.CaseUtils;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.logging.slf4j.Slf4jImpl;
import org.apache.ibatis.plugin.Interceptor;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Expirations;
import org.ehcache.xml.model.TimeUnit;
import org.hibernate.cache.jcache.JCacheHelper;
import org.hibernate.cfg.AvailableSettings;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;

import net.oopscraft.application.core.ValueMap;
import net.oopscraft.application.core.mybatis.CamelCaseValueMap;
import net.oopscraft.application.core.mybatis.PageRowBoundsInterceptor;
import net.oopscraft.application.core.mybatis.YesNoBooleanTypeHandler;
import net.oopscraft.application.core.spring.EncryptedPropertySourceFactory;
import net.oopscraft.application.message.MessageSource;

/**
 * Application Context Configuration
 * @version 0.0.1
 */
@Configuration
@PropertySources({
	@PropertySource(value = "file:conf/application.properties", factory = EncryptedPropertySourceFactory.class)
})
@ComponentScan(
	 nameGenerator = net.oopscraft.application.core.spring.FullBeanNameGenerator.class
	,excludeFilters = @Filter(type=FilterType.ANNOTATION, value= {
		 Configuration.class
		,Controller.class
		,RestController.class
		,ControllerAdvice.class
	})
)
@EnableJpaRepositories(
	 entityManagerFactoryRef = "entityManagerFactory"
	 ,basePackages = "net.oopscraft.application"
)
@MapperScan(
	 annotationClass=Mapper.class
	,nameGenerator = net.oopscraft.application.core.spring.FullBeanNameGenerator.class
	,sqlSessionFactoryRef = "sqlSessionFactory"
	,basePackages = "net.oopscraft.application"
)
public class ApplicationContext {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationContext.class);
	
	@Autowired
	private Environment environment;
	
	static 	DataSource dataSource;
	static 	LocalContainerEntityManagerFactoryBean entityManagerFactory;
	static SqlSessionFactoryBean sqlSessionFactoryBean;
	static MessageSource messageSource;

	/**
	 * Creates dataSource for database connection pool(DBCP)
	 * @return
	 * @throws Exception
	 */
	@Bean(destroyMethod="close")
	public DataSource dataSource() throws Exception {
		LOGGER.info("Creates dataSource");

		// parses dataSource properties
		Properties dataSourceProperties = new Properties();
		dataSourceProperties.put("defaultAutoCommit", false);
		dataSourceProperties.put("driver", environment.getProperty("application.dataSource.driver"));
		dataSourceProperties.put("url", environment.getProperty("application.dataSource.url"));
		dataSourceProperties.put("username", environment.getProperty("application.dataSource.username"));
		dataSourceProperties.put("password", environment.getProperty("application.dataSource.password"));
		dataSourceProperties.put("initialSize", environment.getProperty("application.dataSource.initialSize"));
		dataSourceProperties.put("maxTotal", environment.getProperty("application.dataSource.maxTotal"));
		dataSourceProperties.put("validationQuery", environment.getProperty("application.dataSource.validationQuery"));
		dataSourceProperties.put("jmxName","org.apache.commons.dbcp2:name=dataSource,type=BasicDataSource");
		
		// creates dastaSource instance.
		dataSource = BasicDataSourceFactory.createDataSource(dataSourceProperties);
		
		// returns
		return dataSource;
	}
	
	/**
	 * Creates JPA entity manager factory bean.
	 * @return
	 * @throws Exception
	 */
	@Bean
	@DependsOn({"dataSource"})
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws Exception {
		
		// creates LocalContainerEntityManagerFactoryBean instance.
		entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
		entityManagerFactory.setDataSource(dataSource);
		entityManagerFactory.setPersistenceUnitName("entityManagerFactory");

		// defines vendor adapter
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        Properties jpaProperties = new Properties();
        jpaProperties.setProperty(AvailableSettings.HQL_BULK_ID_STRATEGY, "org.hibernate.hql.spi.id.inline.InlineIdsOrClauseBulkIdStrategy");	// Bulk-id strategies when you can’t use temporary tables
        jpaProperties.setProperty(AvailableSettings.FORMAT_SQL, "true");
        jpaProperties.setProperty(AvailableSettings.DEFAULT_NULL_ORDERING, "last");
        
        // cache configuration
        jpaProperties.setProperty(AvailableSettings.USE_SECOND_LEVEL_CACHE, "true");
        jpaProperties.setProperty(AvailableSettings.USE_QUERY_CACHE, "true");
        jpaProperties.setProperty(AvailableSettings.CACHE_REGION_FACTORY, "org.hibernate.cache.jcache.JCacheRegionFactory");
//        jpaProperties.setProperty(AvailableSettings.CACHE_PROVIDER_CONFIG, "org.ehcache.jsr107.EhcacheCachingProvider");
        

        
//        CachingProvider provider = Caching.getCachingProvider(); 
//        CacheManager cacheManager = provider.getCacheManager();
//        MutableConfiguration<Long, String> configuration =
//                new MutableConfiguration<Long, String>()  
//                    .setTypes(Long.class, String.class)   
//                    .setStoreByValue(false)   
//                    .setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(new Duration(SECONDS, 10)));
//        cacheManager.createCache("test", configuration);
//        
//        CacheConfiguration<Long, String> cacheConfiguration = CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class,
//                ResourcePoolsBuilder.heap(100)) 
//            .withExpiry(Expirations.timeToLiveExpiration(new Duration(TimeUnit.SECONDS, 3))) 
//            .build();
        
        

        // generates DDL options
		String generateDdl = System.getProperty("application.entityManagerFactory.generateDdl");
		if("true".equals(generateDdl)) {
			vendorAdapter.setGenerateDdl(true);
			jpaProperties.setProperty(AvailableSettings.HBM2DDL_AUTO, "create-drop");
			jpaProperties.setProperty(AvailableSettings.HBM2DDL_IMPORT_FILES, "/net/oopscraft/application/import.sql");
			jpaProperties.setProperty(AvailableSettings.HBM2DDL_IMPORT_FILES_SQL_EXTRACTOR, org.hibernate.tool.hbm2ddl.MultipleLinesSqlCommandExtractor.class.getName());
		}else {
			vendorAdapter.setGenerateDdl(false);
		}
		
		vendorAdapter.setDatabasePlatform(environment.getProperty("application.entityManagerFactory.databasePlatform"));
        entityManagerFactory.setJpaVendorAdapter(vendorAdapter);
        entityManagerFactory.setJpaProperties(jpaProperties);
        
        // sets packagesToScan property.
		List<String> packagesToScans = new ArrayList<String>();
		packagesToScans.add(this.getClass().getPackage().getName());
		String packagesToScan = environment.getProperty("application.entityManagerFactory.packagesToScan");
		for(String element : packagesToScan.split(",")) {
			if(element.trim().length() > 0) {
				packagesToScans.add(element.trim());
			}
		}
        entityManagerFactory.setPackagesToScan(packagesToScans.toArray(new String[packagesToScans.size()-1]));
        
        // return
        return entityManagerFactory;
	}	

	/**
	 * Creates MYBIATIS SqlSessionFactory bean.
	 * @return
	 * @throws Exception
	 */
	@Bean
	@DependsOn({"dataSource"})
	public SqlSessionFactoryBean sqlSessionFactory() throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource);
		
		// sets configurations
		org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
		configuration.setCacheEnabled(true);
		configuration.setCallSettersOnNulls(true);
		configuration.setMapUnderscoreToCamelCase(true);
		configuration.setDatabaseId(environment.getProperty("application.sqlSessionFactory.databaseId"));
		configuration.setLogImpl(Slf4jImpl.class);
		configuration.getTypeAliasRegistry().registerAlias(CaseUtils.toCamelCase(ValueMap.class.getSimpleName(),false), CamelCaseValueMap.class);
		configuration.getTypeAliasRegistry().registerAlias(CaseUtils.toCamelCase(YesNoBooleanTypeHandler.class.getSimpleName(),false), YesNoBooleanTypeHandler.class);
		sqlSessionFactoryBean.setConfiguration(configuration);
		
		// sets intercepter instance
		sqlSessionFactoryBean.setPlugins(new Interceptor[] {
			new PageRowBoundsInterceptor()
		});
		
		// sets mapLocations
		Vector<Resource> mapperLocationResources = new Vector<Resource>();
		PathMatchingResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
		String mapperLocations = environment.getProperty("application.sqlSessionFactory.mapperLocations");
		for(String mapperLocation : mapperLocations.split(",")) {
			if(mapperLocation.trim().length() > 0) {
				for(Resource mapperLocationResource : resourceResolver.getResources(mapperLocation)) {
					mapperLocationResources.add(mapperLocationResource);
				}
			}
		}
		sqlSessionFactoryBean.setMapperLocations(mapperLocationResources.toArray(new Resource[mapperLocationResources.size()]));
		
		// invokes afterPropertiesSet method
		sqlSessionFactoryBean.afterPropertiesSet();
		return sqlSessionFactoryBean;
	}

	/**
	 * Creates chained transaction manager bean.
	 * @return
	 * @throws Exception
	 */
	@Bean
	@DependsOn({"dataSource","entityManagerFactory"})
	public PlatformTransactionManager transactionManager() throws Exception {
		
		// JPA transactionManager
		JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
		jpaTransactionManager.setEntityManagerFactory(entityManagerFactory.getObject());
		
		// MYBATIS transactionManager
		DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
		dataSourceTransactionManager.setDataSource(dataSource);
		
		// creates chained transaction manager
		ChainedTransactionManager transactionManager = new ChainedTransactionManager(jpaTransactionManager, dataSourceTransactionManager);
		return transactionManager;
	}
	
	/**
	 * Creates message source bean.
	 * @return
	 * @throws Exception
	 */
	@Bean
	public MessageSource messageSource() throws Exception {
		messageSource = new MessageSource();
		messageSource.setBasename("classpath:net/oopscraft/application/message");
		messageSource.setFallbackToSystemLocale(false);
		messageSource.setDefaultEncoding("UTF-8");
		messageSource.setUseCodeAsDefaultMessage(true);
		messageSource.setCacheSeconds(10);
		return messageSource;
	}



}
