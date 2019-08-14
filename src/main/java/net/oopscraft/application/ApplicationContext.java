package net.oopscraft.application;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.logging.slf4j.Slf4jImpl;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
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
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;

import net.oopscraft.application.core.mybatis.PageInterceptor;

/**
 * Application Context Configuration
 * @author chomookun@gmail.com
 * @version 0.0.1
 * @see    None
 */
@EnableJpaRepositories(
	basePackages = "net.oopscraft.application",
	entityManagerFactoryRef = "entityManagerFactory"
)
@MapperScan(
	basePackages = "net.oopscraft.application",
	annotationClass=Mapper.class,
	nameGenerator = net.oopscraft.application.core.spring.FullBeanNameGenerator.class,
	sqlSessionFactoryRef = "sqlSessionFactory"
)
@ComponentScan(
	basePackages = "net.oopscraft.application",
	nameGenerator = net.oopscraft.application.core.spring.FullBeanNameGenerator.class,
	lazyInit = true,
	excludeFilters = @Filter(type=FilterType.ANNOTATION, value= {Controller.class,RestController.class,ControllerAdvice.class,EnableWebSecurity.class})
)
public class ApplicationContext {
	
	static String propertiesPath = "conf/application.properties";
	static Properties properties;
	
	DataSource dataSource;
	LocalContainerEntityManagerFactoryBean entityManagerFactory;
	SqlSessionFactoryBean sqlSessionFactoryBean;
	
	/*
	 * Creates static resources 
	 */
	static {
		try {
			properties = PropertiesLoaderUtils.loadProperties(new FileSystemResource(new File(propertiesPath)));
		}catch(Exception e) {
			e.printStackTrace(System.err);
		}
	}

	/**
	 * Creates PropertyPlaceHolderConfigurer bean.
	 * @return
	 * @throws Exception
	 */
	@Bean
	public static PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() throws Exception {
		PropertyPlaceholderConfigurer propertyPlaceholderConfigurer = new PropertyPlaceholderConfigurer();
		propertyPlaceholderConfigurer.setProperties(properties);
		return propertyPlaceholderConfigurer;
	}
	
	/**
	 * Creates dataSource for database connection pool(DBCP)
	 * @return
	 * @throws Exception
	 */
	@Bean(destroyMethod="close")
	public DataSource dataSource() throws Exception {

		// parses dataSource properties
		Properties datasourceProperties = new Properties();
		datasourceProperties.put("defaultAutoCommit", false);
		datasourceProperties.put("driver", properties.getProperty("application.dataSource.driver"));
		datasourceProperties.put("url", properties.getProperty("application.dataSource.url"));
		datasourceProperties.put("username", properties.getProperty("application.dataSource.username"));
		datasourceProperties.put("password", properties.getProperty("application.dataSource.password"));
		datasourceProperties.put("initialSize", properties.getProperty("application.dataSource.initialSize"));
		datasourceProperties.put("maxActive", properties.getProperty("application.dataSource.maxActive"));
		datasourceProperties.put("validationQuery", properties.getProperty("application.dataSource.validationQuery"));
		
		// creates dastaSource instance.
		dataSource = BasicDataSourceFactory.createDataSource(datasourceProperties);
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

		// defines vendor adapter
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(false);
        vendorAdapter.setShowSql(false);
        vendorAdapter.setDatabasePlatform(properties.getProperty("application.entityManagerFactory.databasePlatform"));
        entityManagerFactory.setJpaVendorAdapter(vendorAdapter);

        // sets packagesToScan property.
		List<String> packagesToScans = new ArrayList<String>();
		packagesToScans.add(this.getClass().getPackage().getName());
		String packagesToScan = properties.getProperty("application.entityManagerFactory.packagesToScan");
		for(String element : packagesToScan.split(",")) {
			if(element.trim().length() > 0) {
				packagesToScans.add(element.trim());
			}
		}
        entityManagerFactory.setPackagesToScan(packagesToScans.toArray(new String[packagesToScans.size()-1]));
        
        // JPA properties
        Properties jpaProperties = new Properties();
        jpaProperties.setProperty("hibernate.hql.bulk_id_strategy", "org.hibernate.hql.spi.id.inline.InlineIdsOrClauseBulkIdStrategy");	// Bulk-id strategies when you can’t use temporary tables	
        entityManagerFactory.setJpaProperties(jpaProperties);
        
        entityManagerFactory.afterPropertiesSet();
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
		configuration.setDatabaseId(properties.getProperty("application.sqlSessionFactory.databaseId"));
		configuration.setLogImpl(Slf4jImpl.class);
		sqlSessionFactoryBean.setConfiguration(configuration);
		
		// sets intercepter instance
		sqlSessionFactoryBean.setPlugins(new Interceptor[] {
			new PageInterceptor()
		});
		
		// sets mapLocations
		Vector<Resource> mapperLocationResources = new Vector<Resource>();
		PathMatchingResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
		for(Resource mapperLocationResource : resourceResolver.getResources("classpath*:net/oopscraft/application/**/*Mapper.xml")) {
			mapperLocationResources.add(mapperLocationResource);
		}
		String mapperLocations = properties.getProperty("application.sqlSessionFactory.mapperLocations");
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
	public PlatformTransactionManager transactionManager() throws Exception {
		
		// JPA transactionManager
		JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
		jpaTransactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
		
		// MYBATIS transactionManager
		DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
		dataSourceTransactionManager.setDataSource(dataSource());
		
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
	public ReloadableResourceBundleMessageSource messageSource() throws Exception {
		ReloadableResourceBundleMessageSource applicationMessageSource = new ReloadableResourceBundleMessageSource();
		applicationMessageSource.setBasename("classpath:conf/i18n/message");
		applicationMessageSource.setFallbackToSystemLocale(false);
		applicationMessageSource.setDefaultEncoding("UTF-8");
		applicationMessageSource.setUseCodeAsDefaultMessage(true);
		applicationMessageSource.setCacheSeconds(10);
		return applicationMessageSource;
	}

}
