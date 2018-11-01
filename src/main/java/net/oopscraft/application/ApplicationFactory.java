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
import javax.persistence.SharedCacheMode;
import javax.persistence.ValidationMode;
import javax.persistence.spi.ClassTransformer;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.apache.commons.lang.StringUtils;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import net.oopscraft.application.core.PasswordBasedEncryptor;
import net.oopscraft.application.core.WebServer;
import net.oopscraft.application.core.WebServerContext;
import net.oopscraft.application.core.XPathReader;

public class ApplicationFactory {
	
	private static final String PROPERTY_IDENTIFIER = "\\$\\{(.*?)\\}";
	
	/**
	 * getApplication
	 * @param clazz
	 * @param applicationXmlFile
	 * @param applicationPropertiesFile
	 * @return
	 * @throws Exception
	 */
	public static Application getApplication(Class<?> clazz, File applicationXmlFile, File applicationPropertiesFile) throws Exception {
		
		Application application = (Application) clazz.newInstance();
		application.setXmlFile(applicationXmlFile);
		application.setPropertiesFile(applicationPropertiesFile);

		// creates resources.
		XPathReader contextXmlReader = new XPathReader(applicationXmlFile);
		Properties contextProperties = new Properties();
		contextProperties.load(new FileInputStream(applicationPropertiesFile));
		application.setWebServers(createWebServers(contextXmlReader, contextProperties));
		application.setDataSources(createDataSources(contextXmlReader, contextProperties));
		application.setEntityManagerFactories(createEntityManagerFactories(contextXmlReader, contextProperties));
		
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
	 * Creates Web Server instance.
	 * @param context
	 * @throws Exception
	 */
	private static Map<String,WebServer> createWebServers(XPathReader contextXmlReader, Properties contextProperties) throws Exception {
		HashMap<String,WebServer> webServers = new LinkedHashMap<String,WebServer>();
		NodeList webServerNodeList = (NodeList) contextXmlReader.getElement("/application/webServer");
		for(int idx = 1; idx <= webServerNodeList.getLength(); idx ++ ) {
			String webServerExpression = String.format("/application/webServer[%d]", idx);
			String id = contextXmlReader.getTextContent(webServerExpression + "/@id");
			int port = Integer.parseInt(contextXmlReader.getTextContent(webServerExpression + "/@port"));
			WebServer webServer = new WebServer();
			webServer.setPort(port);
			
			// setting SSL properties
			boolean ssl = Boolean.parseBoolean(contextXmlReader.getTextContent(webServerExpression + "/ssl"));
			if(ssl == true) {
				webServer.setSsl(true);
				String keyStorePath = contextXmlReader.getTextContent(webServerExpression + "/ssl/keyStorePath");
				String keyStoreType = contextXmlReader.getTextContent(webServerExpression + "/ssl/keyStoreType");
				String keyStorePass = contextXmlReader.getTextContent(webServerExpression + "/ssl/keyStorePass");
				webServer.setKeyStorePath(keyStorePath);
				webServer.setKeyStoreType(keyStoreType);
				webServer.setKeyStorePass(parseValue(keyStorePass, contextProperties));
			}
			
			// adds context
			NodeList contextNodeList = (NodeList) contextXmlReader.getElement(webServerExpression + "/context");
			for(int i = 1; i <= contextNodeList.getLength(); i ++) {
				String contextExpression = String.format(webServerExpression + "/context[%d]", i);
				String path = contextXmlReader.getTextContent(contextExpression + "/@path");
				String resourceBase = contextXmlReader.getTextContent(contextExpression + "/resourceBase");
				String descriptor = contextXmlReader.getTextContent(contextExpression + "/descriptor");
				WebServerContext webServerContext = new WebServerContext();
				webServerContext.setContextPath(path);
				webServerContext.setResourceBase(resourceBase);
				webServerContext.setDescriptor(descriptor);
				webServer.addContext(webServerContext);
			}
			
			// add webServer
			webServers.put(id, webServer);
		}
		return webServers;
	}
	
	/**
	 * initializeDataSource
	 * @param configXmlReader
	 * @throws Exception
	 */
	private static HashMap<String,DataSource> createDataSources(XPathReader contextXmlReader, Properties contextProperties) throws Exception {
		HashMap<String,DataSource> dataSources = new LinkedHashMap<String,DataSource>();
		NodeList dataSourceNodeList = (NodeList) contextXmlReader.getElement("/application/dataSource");
		for(int idx = 1; idx <= dataSourceNodeList.getLength(); idx ++ ) {
			String dataSourceExpression = String.format("/application/dataSource[%d]", idx);
			String id = contextXmlReader.getTextContent(dataSourceExpression + "/@id");
			
			// parses dataSource properties
			Properties datasourceProperties = new Properties();
			NodeList propertiesNodeList = (NodeList) contextXmlReader.getElement(dataSourceExpression + "/*");
			for(int i = 0; i < propertiesNodeList.getLength(); i ++) {
				Node propertyNode = propertiesNodeList.item(i);
				String name = propertyNode.getNodeName();
				String value = propertyNode.getTextContent();
				datasourceProperties.setProperty(name, parseValue(StringUtils.defaultString(value), contextProperties));
			}
			
			// creates dastaSource instance.
			DataSource dataSource = BasicDataSourceFactory.createDataSource(datasourceProperties);
			dataSources.put(id, dataSource);
		}
		return dataSources;
	}

	/**
	 * createEntityManagerFactories
	 * @param contextXmlReader
	 * @param contextProperties
	 * @return
	 * @throws Exception
	 */
	private static HashMap<String,EntityManagerFactory> createEntityManagerFactories(XPathReader contextXmlReader, Properties contextProperties) throws Exception {
		HashMap<String,EntityManagerFactory> entityManagerFactories = new LinkedHashMap<String,EntityManagerFactory>();
		PersistenceUnitInfo persistenceUnitInfo = new PersistenceUnitInfo() {

			@Override
			public String getPersistenceUnitName() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getPersistenceProviderClassName() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public PersistenceUnitTransactionType getTransactionType() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public DataSource getJtaDataSource() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public DataSource getNonJtaDataSource() {
				// TODO Auto-generated method stub
				return null;
				
			}

			@Override
			public List<String> getMappingFileNames() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public List<URL> getJarFileUrls() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public URL getPersistenceUnitRootUrl() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public List<String> getManagedClassNames() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean excludeUnlistedClasses() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public SharedCacheMode getSharedCacheMode() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ValidationMode getValidationMode() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Properties getProperties() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getPersistenceXMLSchemaVersion() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ClassLoader getClassLoader() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void addTransformer(ClassTransformer transformer) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public ClassLoader getNewTempClassLoader() {
				// TODO Auto-generated method stub
				return null;
			}
			
		};

		
		HibernatePersistenceProvider hibernatePersistenceProvider = new HibernatePersistenceProvider();

		
		Properties properties = new Properties();
//		properties.put("hibernate.hbm2ddl.auto", "create");
		properties.put("hibernate.show_sql", "false");
		properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        
		EntityManagerFactory entityManagerFactory = hibernatePersistenceProvider.createContainerEntityManagerFactory(persistenceUnitInfo, properties);
		
		entityManagerFactories.put("entityManagerFactory", entityManagerFactory);
		return entityManagerFactories;
	}
	
	
	private static void createSqlSessionFactory() throws Exception {
		
		//SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(config);
		
	}
	
}
