package net.oopscraft.application;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.SharedCacheMode;
import javax.persistence.ValidationMode;
import javax.persistence.spi.ClassTransformer;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.apache.commons.lang.StringUtils;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.orm.jpa.AbstractEntityManagerFactoryBean;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import net.oopscraft.application.core.PasswordBasedEncryptor;
import net.oopscraft.application.core.WebServer;
import net.oopscraft.application.core.WebServerContext;
import net.oopscraft.application.core.XPathReader;

public class ApplicationBuilder {
	
	private static final String PROPERTY_IDENTIFIER = "\\$\\{(.*?)\\}";
	Class<?> clazz;
	File xmlFile;
	File propertiesFile;
	
	public ApplicationBuilder setClass(Class<?> clazz) {
		this.clazz = clazz;
		return this;
	}
	
	public ApplicationBuilder setXmlFile(File xmlFile) {
		this.xmlFile = xmlFile;
		return this;
	}
	
	public ApplicationBuilder setPropertiesFile(File propertiesFile) {
		this.propertiesFile = propertiesFile;
		return this;
	}
	
	public Application build() throws Exception {
		Application application = (Application) clazz.newInstance();
		application.setXmlFile(xmlFile);
		application.setPropertiesFile(propertiesFile);
		XPathReader xPathReader = new XPathReader(xmlFile);
		Properties properties = new Properties();
		properties.load(new FileInputStream(propertiesFile));
		buildWebServers(application, xPathReader, properties);
		buildDataSources(application, xPathReader, properties);
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
			int port = Integer.parseInt(xPathReader.getTextContent(webServerExpression + "/@port"));
			WebServer webServer = new WebServer();
			webServer.setPort(port);
			
			// setting SSL properties
			boolean ssl = Boolean.parseBoolean(xPathReader.getTextContent(webServerExpression + "/ssl"));
			if(ssl == true) {
				webServer.setSsl(true);
				String keyStorePath = xPathReader.getTextContent(webServerExpression + "/ssl/keyStorePath");
				String keyStoreType = xPathReader.getTextContent(webServerExpression + "/ssl/keyStoreType");
				String keyStorePass = xPathReader.getTextContent(webServerExpression + "/ssl/keyStorePass");
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
	
	void buildEntityManagerFactories(Application application, XPathReader xPathReader, Properties properties) throws Exception {
		Map<String,LocalContainerEntityManagerFactoryBean> entityManagerFactories = new LinkedHashMap<String,LocalContainerEntityManagerFactoryBean>();
		
        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setDataSource(application.getDataSource("dataSource"));
        entityManagerFactory.setPackagesToScan(new String[] {"net.oopscraft.application.user"});

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(false);
        vendorAdapter.setDatabasePlatform("org.hibernate.dialect.MySQLDialect");
        vendorAdapter.setShowSql(true);

        entityManagerFactory.setJpaVendorAdapter(vendorAdapter);
        
        entityManagerFactory.afterPropertiesSet();
        
        entityManagerFactories.put("entityManagerFactory", entityManagerFactory);
		application.setEntityManagerFactories(entityManagerFactories);
	}

//	/**
//	 * buildEntityManagerFactories
//	 * @param xPathReader
//	 * @param properties
//	 * @return
//	 * @throws Exception
//	 */
//	void buildEntityManagerFactories(Application application, XPathReader xPathReader, Properties properties) throws Exception {
//		HashMap<String,EntityManagerFactory> entityManagerFactories = new LinkedHashMap<String,EntityManagerFactory>();
//		final DataSource dataSource = application.getDataSource("dataSource");
//		PersistenceUnitInfo persistenceUnitInfo = new PersistenceUnitInfo() {
//
//			@Override
//			public String getPersistenceUnitName() {
//				// TODO Auto-generated method stub
//				return null;
//			}
//
//			@Override
//			public String getPersistenceProviderClassName() {
//				// TODO Auto-generated method stub
//				return null;
//			}
//
//			@Override
//			public PersistenceUnitTransactionType getTransactionType() {
//				// TODO Auto-generated method stub
//				return null;
//			}
//
//			@Override
//			public DataSource getJtaDataSource() {
//				// TODO Auto-generated method stub
//				return null;
//			}
//
//			@Override
//			public DataSource getNonJtaDataSource() {
//				return dataSource;
//			}
//
//			@Override
//			public List<String> getMappingFileNames() {
//				// TODO Auto-generated method stub
//				return null;
//			}
//
//			@Override
//			public List<URL> getJarFileUrls() {
//				// TODO Auto-generated method stub
//				return null;
//			}
//
//			@Override
//			public URL getPersistenceUnitRootUrl() {
//				// TODO Auto-generated method stub
//				return null;
//			}
//
//			@Override
//			public List<String> getManagedClassNames() {
//				// TODO Auto-generated method stub
//				return null;
//			}
//
//			@Override
//			public boolean excludeUnlistedClasses() {
//				// TODO Auto-generated method stub
//				return false;
//			}
//
//			@Override
//			public SharedCacheMode getSharedCacheMode() {
//				// TODO Auto-generated method stub
//				return null;
//			}
//
//			@Override
//			public ValidationMode getValidationMode() {
//				// TODO Auto-generated method stub
//				return null;
//			}
//
//			@Override
//			public Properties getProperties() {
//				// TODO Auto-generated method stub
//				return null;
//			}
//
//			@Override
//			public String getPersistenceXMLSchemaVersion() {
//				// TODO Auto-generated method stub
//				return null;
//			}
//
//			@Override
//			public ClassLoader getClassLoader() {
//				// TODO Auto-generated method stub
//				return null;
//			}
//
//			@Override
//			public void addTransformer(ClassTransformer transformer) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public ClassLoader getNewTempClassLoader() {
//				// TODO Auto-generated method stub
//				return null;
//			}
//			
//		};
//		
//		HibernatePersistenceProvider hibernatePersistenceProvider = new HibernatePersistenceProvider();
//		
//		Map<String,String> propertiesMap = new LinkedHashMap<String,String>();
////		map.put("hibernate.hbm2ddl.auto", "create");
//		propertiesMap.put("hibernate.show_sql", "true");
//		propertiesMap.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
//		
//		EntityManagerFactory entityManagerFactory = hibernatePersistenceProvider.createContainerEntityManagerFactory(persistenceUnitInfo, propertiesMap);
//		
//		entityManagerFactories.put("entityManagerFactory", entityManagerFactory);
//
//		application.setEntityManagerFactories(entityManagerFactories);
//	}
	
//	
//	private static void createSqlSessionFactory() throws Exception {
//		
//		//SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(config);
//		
//	}
	
}
