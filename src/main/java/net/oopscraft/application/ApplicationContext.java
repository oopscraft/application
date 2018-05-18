package net.oopscraft.application;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasypt.contrib.org.apache.commons.codec_1_3.binary.Base64;
import org.w3c.dom.NodeList;

import net.oopscraft.application.core.SqlSessionProxyFactory;
import net.oopscraft.application.core.WebServer;
import net.oopscraft.application.core.WebServerContext;
import net.oopscraft.application.core.XPathReader;

public class ApplicationContext {
	
	private static final String LOGO = new String(Base64.decodeBase64("ICBfX19fICBfX19fICBfX18gIF9fX19fX19fX19fXyAgX19fICAgX19fX19fX19fXyAgICBfX18gICBfX18gIF9fXyAgX18gICBfX19fX19fX19fX18gX19fX19fX19fX19fX18gIF8gIF9fDQogLyBfXyBcLyBfXyBcLyBfIFwvIF9fLyBfX18vIF8gXC8gXyB8IC8gX18vXyAgX18vX19fLyBfIHwgLyBfIFwvIF8gXC8gLyAgLyAgXy8gX19fLyBfIC9fICBfXy8gIF8vIF9fIFwvIHwvIC8NCi8gL18vIC8gL18vIC8gX19fL1wgXC8gL19fLyAsIF8vIF9fIHwvIF8vICAvIC8gL19fXy8gX18gfC8gX19fLyBfX18vIC9fX18vIC8vIC9fXy8gX18gfC8gLyBfLyAvLyAvXy8gLyAgICAvIA0KXF9fX18vXF9fX18vXy8gIC9fX18vXF9fXy9fL3xfL18vIHxfL18vICAgL18vICAgICAvXy8gfF8vXy8gIC9fLyAgL19fX18vX19fL1xfX18vXy8gfF8vXy8gL19fXy9cX19fXy9fL3xfLyAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA=".getBytes()));
	private static final Log LOG = LogFactory.getLog(ApplicationContext.class);
	private static ApplicationContext instance;
	
	private File configureFile;
	private File propertiesFile;
	protected Map<String,WebServer> webServers = new LinkedHashMap<String,WebServer>();
	protected Map<String,SqlSessionProxyFactory> sqlSessionProxyFactories = new LinkedHashMap<String,SqlSessionProxyFactory>();
	
	public synchronized static void initialize(File configureFile, File propertiesFile) throws Exception {
		synchronized(ApplicationContext.class) {
			if(instance == null) {
				instance = new ApplicationContext(configureFile, propertiesFile);
			}
		}
	}
	
	/**
	 * getInstance
	 * @return
	 * @throws Exception
	 */
	public synchronized static ApplicationContext getInstance() throws Exception {
		synchronized(ApplicationContext.class) {
			if(instance == null) {
				throw new Exception("ApplicationContext is not initialized.");
			}
		}
		return instance;
	}
	
	/**
	 * constructor
	 * @throws Exception
	 */
	private ApplicationContext(File configureFile, File propertiesFile) throws Exception {
		this.configureFile = configureFile;
		this.propertiesFile = propertiesFile;
		prepareLog();
		printWelcomeMessage();
		createSqlSessionProxyFactory();
		createWebServers();
	}
	
	/**
	 * Creates Log
	 * @param application
	 * @throws Exception
	 */
	private void prepareLog() throws Exception {
		XPathReader xmlReader = new XPathReader(configureFile);
		String loggerExpression = "/application/logger";
		String loggerClass = xmlReader.getTextContent(loggerExpression + "/@class");
		System.setProperty(org.apache.commons.logging.Log.class.getName(), loggerClass);
		
		// loading log4j configuration file
		if(org.apache.commons.logging.impl.Log4JLogger.class.getName().equals(loggerClass)){
			String configureFile = xmlReader.getTextContent(loggerExpression + "/configureFile");
			org.apache.log4j.xml.DOMConfigurator.configureAndWatch(configureFile);
		}
	}
	
	/**
	 * printWelcomeMessage
	 * @throws Exception
	 */
	private static void printWelcomeMessage() throws Exception {
		
		// print text LOGO
		LOG.info("\n" + LOGO);
		
		// print all class path
		LOG.info("+ List of Lirary");
		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
		URL[] urls = ((URLClassLoader)classLoader).getURLs();
		for(URL url : urls) {
			LOG.info(url.getFile());
		}
	}
	
	/**
	 * Creates Web Server instance.
	 * @param context
	 * @throws Exception
	 */
	private void createWebServers() throws Exception {
		XPathReader xmlReader = new XPathReader(configureFile);
		NodeList webServerNodeList = (NodeList) xmlReader.getElement("/application/webServer");
		for(int idx = 1; idx <= webServerNodeList.getLength(); idx ++ ) {
			String webServerExpression = String.format("/application/webServer[%d]", idx);
			String id = xmlReader.getTextContent(webServerExpression + "/@id");
			int port = Integer.parseInt(xmlReader.getTextContent(webServerExpression + "/@port"));
			WebServer webServer = new WebServer();
			webServer.setPort(port);
			
			// setting SSL properties
			if(xmlReader.hasElement(webServerExpression + "/ssl")) {
				webServer.setSsl(true);
				String keyStorePath = xmlReader.getTextContent(webServerExpression + "/ssl/keyStorePath");
				String keyStoreType = xmlReader.getTextContent(webServerExpression + "/ssl/keyStoreType");
				String keyStorePass = xmlReader.getTextContent(webServerExpression + "/ssl/keyStorePass");
				webServer.setKeyStorePath(keyStorePath);
				webServer.setKeyStoreType(keyStoreType);
				webServer.setKeyStorePass(keyStorePass);
			}
			
			// adds context
			NodeList contextNodeList = (NodeList) xmlReader.getElement(webServerExpression + "/context");
			for(int i = 1; i <= contextNodeList.getLength(); i ++) {
				String contextExpression = String.format(webServerExpression + "/context[%d]", i);
				String path = xmlReader.getTextContent(contextExpression + "/@path");
				String resourceBase = xmlReader.getTextContent(contextExpression + "/resourceBase");
				String descriptor = xmlReader.getTextContent(contextExpression + "/descriptor");
				WebServerContext webServerContext = new WebServerContext();
				webServerContext.setContextPath(path);
				webServerContext.setResourceBase(resourceBase);
				webServerContext.setDescriptor(descriptor);
				webServer.addContext(webServerContext);
			}

			// add webServer
			webServers.put(id, webServer);
		}
	}
	
	/**
	 * Creates SqlSessionProxyFactory
	 * @param context
	 * @throws Exception
	 */
	private void createSqlSessionProxyFactory() throws Exception {
		XPathReader xmlReader = new XPathReader(configureFile);
		NodeList sqlSessionProxyFactoryNodeList = (NodeList) xmlReader.getElement("/application/sqlSessionProxyFactory");
		for(int idx = 1; idx <= sqlSessionProxyFactoryNodeList.getLength(); idx ++ ) {
			String sqlSessionProxyFactoryExpression = String.format("/application/sqlSessionProxyFactory[%d]", idx);
			String id = xmlReader.getTextContent(sqlSessionProxyFactoryExpression + "/@id");
			String configureFile = xmlReader.getTextContent(sqlSessionProxyFactoryExpression + "/configureFile");
			SqlSessionProxyFactory sqlSessionProxyFactory = SqlSessionProxyFactory.getInstance(new File(configureFile), propertiesFile);
			sqlSessionProxyFactories.put(id, sqlSessionProxyFactory);
		}
	}
	
	public Map<String,WebServer> getWebServers() {
		return webServers;
	}
	
	public WebServer getWebServer(String id) throws Exception {
		if(!webServers.containsKey(id)) {
			throw new Exception(String.format("WebServer[%s] is invalid.", id));
		}
		return webServers.get(id);
	}
	
	public Map<String,SqlSessionProxyFactory> getSqlSessionProxyFactories() {
		return sqlSessionProxyFactories;
	}
	
	public SqlSessionProxyFactory getSqlSessionProxyFactory(String id) throws Exception {
		if(!sqlSessionProxyFactories.containsKey(id)) {
			throw new Exception(String.format("SqlSessionProxyFactory[%s] is invalid.", id));
		}
		return sqlSessionProxyFactories.get(id);
	}
	
}
