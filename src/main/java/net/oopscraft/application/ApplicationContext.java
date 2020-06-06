package net.oopscraft.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.apache.commons.text.CaseUtils;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.logging.slf4j.Slf4jImpl;
import org.apache.ibatis.plugin.Interceptor;
import org.hibernate.cfg.AvailableSettings;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.PropertiesLoaderUtils;
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

import net.oopscraft.application.core.PasswordBasedEncryptor;
import net.oopscraft.application.core.ValueMap;
import net.oopscraft.application.core.mybatis.CamelCaseValueMap;
import net.oopscraft.application.core.mybatis.PageRowBoundsInterceptor;
import net.oopscraft.application.core.mybatis.YesNoBooleanTypeHandler;
import net.oopscraft.application.message.MessageSource;

/**
 * Application Context Configuration
 * @version 0.0.1
 */
@Configuration
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
	
	public static ApplicationConfig applicationConfig;
	static 	DataSource dataSource;
	static 	LocalContainerEntityManagerFactoryBean entityManagerFactory;
	static SqlSessionFactoryBean sqlSessionFactoryBean;
	static MessageSource messageSource;

	@Bean
	public ApplicationConfig applicationConfig() throws Exception {
		LOGGER.info("Creates applicationConfig");
		FileSystemResource resource = new FileSystemResource("conf/application.properties");
		Properties properties = PropertiesLoaderUtils.loadProperties(resource);
		PasswordBasedEncryptor pbEncryptor = new PasswordBasedEncryptor();
		for(String propertyName : properties.stringPropertyNames()) {
			String value = properties.getProperty(propertyName);
			properties.put(propertyName, pbEncryptor.decryptIdentifiedValue(value));
		}
		ConfigurationPropertySource propertySource = new MapConfigurationPropertySource(properties);
		Binder binder = new Binder(propertySource);
		applicationConfig = binder.bind("application", ApplicationConfig.class).get();
		LOGGER.info("applicationConfig:{}", applicationConfig);
		return applicationConfig;
	}
	
	/**
	 * Creates dataSource for database connection pool(DBCP)
	 * @return
	 * @throws Exception
	 */
	@Bean(destroyMethod="close")
	@DependsOn({"applicationConfig"})
	public DataSource dataSource() throws Exception {
		LOGGER.info("Creates dataSource");

		// parses dataSource properties
		Properties dataSourceProperties = new Properties();
		dataSourceProperties.put("defaultAutoCommit", false);
		dataSourceProperties.put("driver", applicationConfig.getDataSource().getDriver());
		dataSourceProperties.put("url", applicationConfig.getDataSource().getUrl());
		dataSourceProperties.put("username", applicationConfig.getDataSource().getUsername());
		dataSourceProperties.put("password", applicationConfig.getDataSource().getPassword());
		dataSourceProperties.put("initialSize", applicationConfig.getDataSource().getInitialSize());
		dataSourceProperties.put("maxTotal", applicationConfig.getDataSource().getMaxTotal());
		dataSourceProperties.put("validationQuery", applicationConfig.getDataSource().getValidationQuery());
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
        jpaProperties.setProperty(AvailableSettings.CACHE_REGION_FACTORY, net.oopscraft.application.core.jpa.JCacheRegionFactory.class.getName());

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
		
		vendorAdapter.setDatabasePlatform(applicationConfig.getEntityManagerFactory().getDatabasePlatform());
        entityManagerFactory.setJpaVendorAdapter(vendorAdapter);
        entityManagerFactory.setJpaProperties(jpaProperties);
        
        // sets packagesToScan property.
		List<String> packagesToScans = new ArrayList<String>();
		packagesToScans.add(this.getClass().getPackage().getName());
		String packagesToScan = applicationConfig.getEntityManagerFactory().getPackagesToScan();
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
		configuration.setDatabaseId(applicationConfig.getSqlSessionFactory().getDatabaseId());
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
		String mapperLocations = applicationConfig.getSqlSessionFactory().getMapperLocations();
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
