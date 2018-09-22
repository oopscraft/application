package net.oopscraft.application;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasypt.contrib.org.apache.commons.codec_1_3.binary.Base64;

import net.oopscraft.application.core.JsonUtils;
import net.oopscraft.application.core.ValueMap;

public class ApplicationContainer {
	
	private static final Log LOGGER = LogFactory.getLog(ApplicationContainer.class);
	private static final String BANNER = new String(Base64.decodeBase64("ICBfX19fICBfX19fICBfX18gIF9fX19fX19fX19fXyAgX19fICAgX19fX19fX19fXyAgICBfX18gICBfX18gIF9fXyAgX18gICBfX19fX19fX19fX18gX19fX19fX19fX19fX18gIF8gIF9fDQogLyBfXyBcLyBfXyBcLyBfIFwvIF9fLyBfX18vIF8gXC8gXyB8IC8gX18vXyAgX18vX19fLyBfIHwgLyBfIFwvIF8gXC8gLyAgLyAgXy8gX19fLyBfIC9fICBfXy8gIF8vIF9fIFwvIHwvIC8NCi8gL18vIC8gL18vIC8gX19fL1wgXC8gL19fLyAsIF8vIF9fIHwvIF8vICAvIC8gL19fXy8gX18gfC8gX19fLyBfX18vIC9fX18vIC8vIC9fXy8gX18gfC8gLyBfLyAvLyAvXy8gLyAgICAvIA0KXF9fX18vXF9fX18vXy8gIC9fX18vXF9fXy9fL3xfL18vIHxfL18vICAgL18vICAgICAvXy8gfF8vXy8gIC9fLyAgL19fX18vX19fL1xfX18vXy8gfF8vXy8gL19fXy9cX19fXy9fL3xfLyAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA=".getBytes()));
	private static final File CONTEXT_FILE = new File("conf/application.json");
	
	static Application application;
	
	/**
	 * main
	 * @param args
	 * @throws Exception
	 */
	public static void main(final String[] args) throws Exception {
		
		// setting log4j2 configuration path
		System.setProperty("log4j.configurationFile", "conf/log4j2.xml");
		
		// prints banner
		LOGGER.info(System.lineSeparator() + BANNER);

		// initiates application
		String applicationJson = FileUtils.readFileToString(CONTEXT_FILE, "UTF-8");
		String className = JsonUtils.convertJsonToObject(applicationJson, ValueMap.class).getString("class");
		Class<?> applicationClass = Class.forName(className);
		application = (Application) JsonUtils.convertJsonToObject(applicationJson, applicationClass);
		application.getWebServer().start();
		
		// executes onStart method
		application.onStart();
		
		// hooking kill signal
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable(){
			public void run() {
				// stop application
				try {
					application.onStop();
				}catch(Exception e){
					e.printStackTrace(System.err);
					LOGGER.warn(e.getMessage(), e);
				}
				// release application
				try {
					application.getWebServer().stop();
				}catch(Exception e){
					e.printStackTrace(System.err);
					LOGGER.warn(e.getMessage(), e);
				}
			}
		}));
		
		// thread join
		Thread.currentThread().join();
	}

//	/**
//	 * launch
//	 * @param application
//	 * @param args
//	 * @throws Exception
//	 */
//	public static void launch(Class<? extends Application> applicationClass, File configureFile, File propertiesFile) {
//		try{
//			// loading log4j2 configuration file.
//			Configurator.initialize(null, "conf/log4j2.xml");
//			
//			// creates ApplicationContext	
//			ApplicationContext.initialize(configureFile, propertiesFile);
//			applicationContext = ApplicationContext.getInstance();
//			
//			// create resource
//			printWelcomeMessage();
//			createSqlSessionProxyFactories();
//			createWebServers();
//			
//			// start application
//			application = applicationClass.newInstance();
//			application.onStart();
//			
//			// hooking kill signal
//			Runtime.getRuntime().addShutdownHook(new Thread(new Runnable(){
//				public void run() {
//					// stop application
//					try {
//						application.onStop();
//					}catch(Exception e){
//						e.printStackTrace(System.err);
//						LOGGER.warn(e.getMessage(), e);
//					}
//					// starts web server
//					for(WebServer webServer : applicationContext.getWebServers().values()) {
//						try {
//							webServer.stop();
//						}catch(Exception e) {
//							e.printStackTrace(System.err);
//							LOGGER.warn(e.getMessage(), e);
//						}
//					}
//				}
//			}));
//			
//			// thread join
//			Thread.currentThread().join();
//			
//		}catch(Exception e){
//			e.printStackTrace(System.err);
//			LOGGER.error(e.getMessage(), e);
//			System.exit(1);
//		}
//	}
//	
//	/**
//	 * Launches application for test
//	 * @param applicationClass
//	 * @param configureFile
//	 * @param propertiesFile
//	 */
//	public static void launchForTest(Class<? extends Application> applicationClass, File configureFile, File propertiesFile) throws Exception {
//		// creates ApplicationContext	
//		ApplicationContext.initialize(configureFile, propertiesFile);
//		applicationContext = ApplicationContext.getInstance();
//		
//		// create resource
//		printWelcomeMessage();
//		createSqlSessionProxyFactories();
//	}
//	
//	/**
//	 * printWelcomeMessage
//	 * @throws Exception
//	 */
//	private static void printWelcomeMessage() throws Exception {
//		LOGGER.info(System.lineSeparator() + BANNER);
//	}
//	
//	
//	/**
//	 * Creates Web Server instance.
//	 * @param context
//	 * @throws Exception
//	 */
//	private static void createWebServers() throws Exception {
//		XPathReader xmlReader = new XPathReader(applicationContext.configureFile);
//		NodeList webServerNodeList = (NodeList) xmlReader.getElement("/application/webServer");
//		for(int idx = 1; idx <= webServerNodeList.getLength(); idx ++ ) {
//			String webServerExpression = String.format("/application/webServer[%d]", idx);
//			String id = xmlReader.getTextContent(webServerExpression + "/@id");
//			int port = Integer.parseInt(xmlReader.getTextContent(webServerExpression + "/@port"));
//			WebServer webServer = new WebServer();
//			webServer.setPort(port);
//			
//			// setting SSL properties
//			if(xmlReader.hasElement(webServerExpression + "/ssl")) {
//				webServer.setSsl(true);
//				String keyStorePath = xmlReader.getTextContent(webServerExpression + "/ssl/keyStorePath");
//				String keyStoreType = xmlReader.getTextContent(webServerExpression + "/ssl/keyStoreType");
//				String keyStorePass = xmlReader.getTextContent(webServerExpression + "/ssl/keyStorePass");
//				webServer.setKeyStorePath(keyStorePath);
//				webServer.setKeyStoreType(keyStoreType);
//				webServer.setKeyStorePass(keyStorePass);
//			}
//			
//			// adds context
//			NodeList contextNodeList = (NodeList) xmlReader.getElement(webServerExpression + "/context");
//			for(int i = 1; i <= contextNodeList.getLength(); i ++) {
//				String contextExpression = String.format(webServerExpression + "/context[%d]", i);
//				String path = xmlReader.getTextContent(contextExpression + "/@path");
//				String resourceBase = xmlReader.getTextContent(contextExpression + "/resourceBase");
//				String descriptor = xmlReader.getTextContent(contextExpression + "/descriptor");
//				WebServerContext webServerContext = new WebServerContext();
//				webServerContext.setContextPath(path);
//				webServerContext.setResourceBase(resourceBase);
//				webServerContext.setDescriptor(descriptor);
//				webServer.addContext(webServerContext);
//			}
//
//			// start server
//			webServer.start();
//			
//			// add webServer
//			applicationContext.webServers.put(id, webServer);
//		}
//	}
//	
//	/**
//	 * Creates SqlSessionProxyFactory
//	 * @param context
//	 * @throws Exception
//	 */
//	private static void createSqlSessionProxyFactories() throws Exception {
//		XPathReader xmlReader = new XPathReader(applicationContext.configureFile);
//		NodeList sqlSessionProxyFactoryNodeList = (NodeList) xmlReader.getElement("/application/sqlSessionProxyFactory");
//		for(int idx = 1; idx <= sqlSessionProxyFactoryNodeList.getLength(); idx ++ ) {
//			String sqlSessionProxyFactoryExpression = String.format("/application/sqlSessionProxyFactory[%d]", idx);
//			String id = xmlReader.getTextContent(sqlSessionProxyFactoryExpression + "/@id");
//			String configureFile = xmlReader.getTextContent(sqlSessionProxyFactoryExpression + "/configureFile");
//			SqlSessionProxyFactory sqlSessionProxyFactory = SqlSessionProxyFactory.getInstance(new File(configureFile), applicationContext.propertiesFile);
//			applicationContext.sqlSessionProxyFactories.put(id, sqlSessionProxyFactory);
//		}
//	}

}
