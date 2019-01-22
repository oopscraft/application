package net.oopscraft.application;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.apache.commons.lang.StringUtils;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import net.oopscraft.application.core.PasswordBasedEncryptor;
import net.oopscraft.application.core.XPathReader;
import net.oopscraft.application.core.webserver.WebServer;
import net.oopscraft.application.core.webserver.WebServerContext;
import net.oopscraft.application.monitor.MonitorAgent;

public class ApplicationBuilder {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationBuilder.class);
	private static final String PROPERTY_IDENTIFIER = "\\$\\{(.*?)\\}";
	Class<?> clazz;
	File xmlFile;
	File propertiesFile;
	
	public ApplicationBuilder setClass(Class<?> clazz) {
		this.clazz = clazz;
		return this;
	}
	
	public ApplicationBuilder setClass(String className) throws Exception {
		this.clazz = Class.forName(className);
		return this;
	}
	
	public ApplicationBuilder setXmlFile(File xmlFile) {
		this.xmlFile = xmlFile;
		return this;
	}
	
	public ApplicationBuilder setXmlFile(String xmlFilePath) {
		this.xmlFile = new File(xmlFilePath);
		return this;
	}

	public ApplicationBuilder setPropertiesFile(File propertiesFile) {
		this.propertiesFile = propertiesFile;
		return this;
	}
	
	public ApplicationBuilder setPropertiesFile(String propertiesFileName) {
		this.propertiesFile = new File(propertiesFileName);
		return this;
	}
	
	/**
	 * build instance
	 * @return
	 * @throws Exception
	 */
	public Application build() throws Exception {
		Application application = (Application) clazz.newInstance();
		application.setXmlFile(xmlFile);
		application.setPropertiesFile(propertiesFile);
		XPathReader xPathReader = new XPathReader(xmlFile);
		Properties properties = new Properties();
		properties.load(new FileInputStream(propertiesFile));
		buildMonitorAgent(application);
		buildConfig(application, xPathReader, properties);
		buildWebServers(application, xPathReader, properties);
		buildDataSources(application, xPathReader, properties);
		buildEntityManagerFactories(application, xPathReader, properties);
		buildSqlSessionFactories(application, xPathReader, properties);
		buildMessageSource(application, xPathReader, properties);
		return application;
	}
	
	/**
	 * parseValue
	 * @param value
	 * @param properties
	 * @return
	 * @throws Exception
	 */
	private static String parseValue(String value, Properties properties) throws Exception {
		Pattern p = Pattern.compile(PROPERTY_IDENTIFIER);
        Matcher m = p.matcher(value);
        StringBuffer sb = new StringBuffer();
        while(m.find()) {
            String propertyName = m.group(1);
            String propertyValue = StringUtils.defaultString(properties.getProperty(propertyName),propertyName);
            String decryptedValue = PasswordBasedEncryptor.decryptIdentifiedValue(propertyValue);
            m.appendReplacement(sb, Matcher.quoteReplacement(decryptedValue));
        }
        m.appendTail(sb);
       return sb.toString();
	}
	
	/**
	 * build MonitorAgent
	 * @param application
	 * @throws Exception
	 */
	void buildMonitorAgent(Application application) throws Exception {
		LOGGER.info("Builds monitorAgent.");
		MonitorAgent.intialize(3, 10);
		MonitorAgent monitorAgent = MonitorAgent.getInstance();
		application.setMonitorAgent(monitorAgent);
	}
	
	/**
	 * builds application config
	 * @param application
	 * @throws Exception
	 */
	void buildConfig(Application application, XPathReader xPathReader, Properties properties) throws Exception {
		NodeList configNodeList = (NodeList) xPathReader.getElement("/application/config/*");
		for(int i = 0, size = configNodeList.getLength(); i < size; i ++) {
			Node configNode = configNodeList.item(i);
			String name = configNode.getNodeName();
			String value = configNode.getTextContent();
			application.setConfig(name, value);
		}
	}
	
	/**
	 * buildWebServer
	 * @param application
	 * @param xPathReader
	 * @param properties
	 * @throws Exception
	 */
	void buildWebServers(Application application, XPathReader xPathReader, Properties properties) throws Exception {
		HashMap<String,WebServer> webServers = new LinkedHashMap<String,WebServer>();
		NodeList webServerNodeList = (NodeList) xPathReader.getElement("/application/webServer");
		for(int idx = 1; idx <= webServerNodeList.getLength(); idx ++ ) {
			String webServerExpression = String.format("/application/webServer[%d]", idx);
			String id = xPathReader.getTextContent(webServerExpression + "/@id");
			String port = parseValue(xPathReader.getTextContent(webServerExpression + "/port"),properties);
			WebServer webServer = new WebServer();
			webServer.setPort(Integer.parseInt(port));
			
			// setting SSL properties
			boolean ssl = Boolean.parseBoolean(xPathReader.getTextContent(webServerExpression + "/ssl"));
			if(ssl == true) {
				webServer.setSsl(true);
				String keyStorePath = xPathReader.getTextContent(webServerExpression + "/keyStorePath");
				String keyStoreType = xPathReader.getTextContent(webServerExpression + "/keyStoreType");
				String keyStorePass = xPathReader.getTextContent(webServerExpression + "/keyStorePass");
				webServer.setKeyStorePath(keyStorePath);
				webServer.setKeyStoreType(keyStoreType);
				webServer.setKeyStorePass(parseValue(keyStorePass, properties));
			}
			
			// adds context
			NodeList contextNodeList = (NodeList) xPathReader.getElement(webServerExpression + "/context");
			for(int i = 1; i <= contextNodeList.getLength(); i ++) {
				String contextExpression = String.format(webServerExpression + "/context[%d]", i);
				String path = xPathReader.getTextContent(contextExpression + "/@path");
				String resourceBase = xPathReader.getTextContent(contextExpression + "/resourceBase");
				String descriptor = xPathReader.getTextContent(contextExpression + "/descriptor");
				WebServerContext webServerContext = new WebServerContext();
				webServerContext.setContextPath(path);
				webServerContext.setResourceBase(resourceBase);
				webServerContext.setDescriptor(descriptor);
				webServer.addContext(webServerContext);
			}
			
			// add webServer
			webServers.put(id, webServer);
		}
		application.setWebServers(webServers);
	}
	
	/**
	 * buildDataSources
	 * @param configXmlReader
	 * @throws Exception
	 */
	void buildDataSources(Application application, XPathReader xPathReader, Properties properties) throws Exception {
		HashMap<String,DataSource> dataSources = new LinkedHashMap<String,DataSource>();
		NodeList dataSourceNodeList = (NodeList) xPathReader.getElement("/application/dataSource");
		for(int idx = 1; idx <= dataSourceNodeList.getLength(); idx ++ ) {
			String dataSourceExpression = String.format("/application/dataSource[%d]", idx);
			String id = xPathReader.getTextContent(dataSourceExpression + "/@id");
			
			// parses dataSource properties
			Properties datasourceProperties = new Properties();
			NodeList propertiesNodeList = (NodeList) xPathReader.getElement(dataSourceExpression + "/*");
			for(int i = 0; i < propertiesNodeList.getLength(); i ++) {
				Node propertyNode = propertiesNodeList.item(i);
				String name = propertyNode.getNodeName();
				String value = propertyNode.getTextContent();
				datasourceProperties.setProperty(name, parseValue(StringUtils.defaultString(value), properties));
			}
			
			// creates dastaSource instance.
			DataSource dataSource = BasicDataSourceFactory.createDataSource(datasourceProperties);
			dataSources.put(id, dataSource);
		}
		application.setDataSources(dataSources);
	}
	
	/**
	 * buildEntityManagerFactories
	 * @param application
	 * @param xPathReader
	 * @param properties
	 * @throws Exception
	 */
	void buildEntityManagerFactories(Application application, XPathReader xPathReader, Properties properties) throws Exception {
		Map<String,LocalContainerEntityManagerFactoryBean> entityManagerFactories = new LinkedHashMap<String,LocalContainerEntityManagerFactoryBean>();
		NodeList entityManagerFactoryNodeList = (NodeList) xPathReader.getElement("/application/entityManagerFactory");
		for(int idx = 1; idx <= entityManagerFactoryNodeList.getLength(); idx ++ ) {
			String entityManagerFactoryExpression = String.format("/application/entityManagerFactory[%d]", idx);
			String id = xPathReader.getTextContent(entityManagerFactoryExpression + "/@id");

			// creates LocalContainerEntityManagerFactoryBean instance.
			LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
	        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
	        vendorAdapter.setGenerateDdl(false);
	        vendorAdapter.setShowSql(true);

	        // sets databasePlatform property
	        String databasePlatform = PasswordBasedEncryptor.decryptIdentifiedValue(xPathReader.getTextContent(entityManagerFactoryExpression + "/databasePlatform"));
	        vendorAdapter.setDatabasePlatform(databasePlatform);
	        entityManagerFactory.setJpaVendorAdapter(vendorAdapter);
	        
			// sets dataSource
	        String dataSource = xPathReader.getTextContent(entityManagerFactoryExpression + "/dataSource");
			entityManagerFactory.setDataSource(application.getDataSource(dataSource));

	        // sets packagesToScan property.
			List<String> packagesToScans = new ArrayList<String>();
			packagesToScans.add(this.getClass().getPackage().getName());
			String packagesToScan = PasswordBasedEncryptor.decryptIdentifiedValue(xPathReader.getTextContent(entityManagerFactoryExpression + "/packagesToScan"));
			for(String element : packagesToScan.split(",")) {
				packagesToScans.add(element.trim());
			}
	        entityManagerFactory.setPackagesToScan(packagesToScans.toArray(new String[packagesToScans.size()-1]));
	        
	        // JPA properties
	        Properties jpaProperties = new Properties();
	        jpaProperties.setProperty("hibernate.hql.bulk_id_strategy", "org.hibernate.hql.spi.id.inline.InlineIdsOrClauseBulkIdStrategy");	// Bulk-id strategies when you can’t use temporary tables	
	        entityManagerFactory.setJpaProperties(jpaProperties);
	        
	        entityManagerFactory.afterPropertiesSet();
	        entityManagerFactories.put(id, entityManagerFactory);
		}
		application.setEntityManagerFactories(entityManagerFactories);
	}
	
	/**
	 * buildSqlSessionFactories
	 * @param application
	 * @param xPathReader
	 * @param properties
	 * @throws Exception
	 */
	void buildSqlSessionFactories(Application application, XPathReader xPathReader, Properties properties) throws Exception {
		Map<String,SqlSessionFactoryBean> sqlSessionFactories = new LinkedHashMap<String,SqlSessionFactoryBean>();
		NodeList sqlSessionFactoryNodeList = (NodeList) xPathReader.getElement("/application/sqlSessionFactory");
		for(int idx = 1; idx <= sqlSessionFactoryNodeList.getLength(); idx ++ ) {
			String sqlSessionFactoryExpression = String.format("/application/sqlSessionFactory[%d]", idx);
			String id = xPathReader.getTextContent(sqlSessionFactoryExpression + "/@id");
			
			// creates sqlSessionFactoryBean instance
			SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
			
			// sets dataSource
			String dataSource = xPathReader.getTextContent(sqlSessionFactoryExpression + "/dataSource");
			sqlSessionFactoryBean.setDataSource(application.getDataSource(dataSource));
			
			// sets mapLocations
			String[] mapperLocations = xPathReader.getTextContent(sqlSessionFactoryExpression + "/mapperLocations").split(",");
			Vector<Resource> mapperLocationResources = new Vector<Resource>();
			PathMatchingResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
			for(String mapperLocation : mapperLocations) {
				for(Resource mapperLocationResource : resourceResolver.getResources(mapperLocation)) {
					mapperLocationResources.add(mapperLocationResource);
				}
			}
			sqlSessionFactoryBean.setMapperLocations(mapperLocationResources.toArray(new Resource[mapperLocationResources.size()]));
			
			// setting configuration properties
			Properties configurationProperties = new Properties();
			NodeList propertiesNodeList = (NodeList) xPathReader.getElement(sqlSessionFactoryExpression + "/configuration/*");
			for(int i = 0; i < propertiesNodeList.getLength(); i ++) {
				Node propertyNode = propertiesNodeList.item(i);
				String name = propertyNode.getNodeName();
				String value = propertyNode.getTextContent();
				configurationProperties.setProperty(name, parseValue(StringUtils.defaultString(value), properties));
			}
			
			// invokes afterPropertiesSet method
			sqlSessionFactoryBean.afterPropertiesSet();
			
			// puts into map
			sqlSessionFactories.put(id, sqlSessionFactoryBean);
		}
		
		// assigns into application instance.
		application.setSqlSessionFactories(sqlSessionFactories);
	}
	
	/**
	 * Builds MessageSource object
	 * @param application
	 * @param xPathReader
	 * @param properties
	 * @throws Exception
	 */
	void buildMessageSource(Application application, XPathReader xPathReader, Properties properties) throws Exception {
		
		// defines system messageSource as parent object.
		ReloadableResourceBundleMessageSource systemMessageSource = new ReloadableResourceBundleMessageSource();
		systemMessageSource.setBasename("classpath:conf/i18n/message");
		systemMessageSource.setFallbackToSystemLocale(false);
		systemMessageSource.setDefaultEncoding("UTF-8");
		systemMessageSource.setUseCodeAsDefaultMessage(true);
		systemMessageSource.setCacheSeconds(10);
		

		
		// creates user defined message sources.
		Map<String,MessageSource> messageSources = new LinkedHashMap<String,MessageSource>();
		NodeList messageSourceNodeList = (NodeList) xPathReader.getElement("/application/messageSource");
		for(int idx = 1; idx <= messageSourceNodeList.getLength(); idx ++ ) {
			String messageSourceExpression = String.format("/application/messageSource[%d]", idx);
			String id = xPathReader.getTextContent(messageSourceExpression + "/@id");
			ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
			String basename = xPathReader.getTextContent(messageSourceExpression + "/basename");
			messageSource.setBasename(basename);
			messageSource.setFallbackToSystemLocale(false);
			messageSource.setDefaultEncoding("UTF-8");
			messageSource.setUseCodeAsDefaultMessage(true);
			String cacheSeconds = xPathReader.getTextContent(messageSourceExpression + "/cacheSeconds");
			messageSource.setCacheSeconds(Integer.parseInt(cacheSeconds));
			messageSources.put(id, messageSource);
			messageSource.setParentMessageSource(systemMessageSource);
		}
		application.setMessageSources(messageSources);
		
		systemMessageSource.getBasenameSet();
		
		
		
	}
	
}
