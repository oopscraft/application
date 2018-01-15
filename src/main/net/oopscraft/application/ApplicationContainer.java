package net.oopscraft.application;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasypt.contrib.org.apache.commons.codec_1_3.binary.Base64;
import org.w3c.dom.NodeList;

import net.oopscraft.application.core.JmxMonitor;
import net.oopscraft.application.core.SqlSessionProxyFactory;
import net.oopscraft.application.core.WebServer;
import net.oopscraft.application.core.WebServerContext;
import net.oopscraft.application.core.XPathReader;

public class ApplicationContainer {

	private static final File CONFIGURE_FILE = new File("conf/application.xml");
	private static final File PROPERTIES_FILE = new File("conf/application.properties");
	private static final String LOGO = new String(Base64.decodeBase64("ICBfX19fICBfX19fICBfX18gIF9fX19fX19fX19fXyAgX19fICAgX19fX19fX19fXyAgICBfX18gICBfX18gIF9fXyAgX18gICBfX19fX19fX19fX18gX19fX19fX19fX19fX18gIF8gIF9fDQogLyBfXyBcLyBfXyBcLyBfIFwvIF9fLyBfX18vIF8gXC8gXyB8IC8gX18vXyAgX18vX19fLyBfIHwgLyBfIFwvIF8gXC8gLyAgLyAgXy8gX19fLyBfIC9fICBfXy8gIF8vIF9fIFwvIHwvIC8NCi8gL18vIC8gL18vIC8gX19fL1wgXC8gL19fLyAsIF8vIF9fIHwvIF8vICAvIC8gL19fXy8gX18gfC8gX19fLyBfX18vIC9fX18vIC8vIC9fXy8gX18gfC8gLyBfLyAvLyAvXy8gLyAgICAvIA0KXF9fX18vXF9fX18vXy8gIC9fX18vXF9fXy9fL3xfL18vIHxfL18vICAgL18vICAgICAvXy8gfF8vXy8gIC9fLyAgL19fX18vX19fL1xfX18vXy8gfF8vXy8gL19fXy9cX19fXy9fL3xfLyAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA=".getBytes()));
	private static final Log LOG = LogFactory.getLog(ApplicationContainer.class);
	private static Application application = null;
	
	/**
	 * launch
	 * @param application
	 * @param args
	 * @throws Exception
	 */
	public static void launch(Class<? extends Application> applicationClass, String[] arguments) {
		try{
			ApplicationContext context = new ApplicationContext(arguments);
			application = applicationClass.getConstructor(ApplicationContext.class).newInstance(context);
			
			// initiates application
			initiateLog();
			
			// prints welcome message
			printWelcomeMessage();
			
			// initiates SqlSessionProxyFactory
			initiateSqlSessionProxyFactory();
			
			// start monitorAgent
			JmxMonitor.intialize(3, 10);
			
			// initiates webServer
			initiateWebServer();
			
			// start application
			application.start();
			
			// hooking kill signal
			Runtime.getRuntime().addShutdownHook(new Thread(new Runnable(){
				public void run() {
					try {
						application.stop();
					}catch(Exception e){
						e.printStackTrace(System.err);
						LOG.warn(e.getMessage(), e);
					}
				}
			}));
			
			// thread join
			Thread.currentThread().join();
			
		}catch(Exception e){
			e.printStackTrace(System.err);
			LOG.error(e.getMessage(), e);
			System.exit(1);
		}
	}
	
	public static void launchForTest(Class<? extends Application> applicationClass, String[] arguments) {
		try{
			ApplicationContext context = new ApplicationContext(arguments);
			application = applicationClass.getConstructor(ApplicationContext.class).newInstance(context);
			
			// initiates application
			initiateLog();
			
			// prints welcome message
			printWelcomeMessage();
			
			// initiates SqlSessionProxyFactory
			initiateSqlSessionProxyFactory();
			
		}catch(Exception e){
			e.printStackTrace(System.err);
			LOG.error(e.getMessage(), e);
			System.exit(1);
		}
	}
	
	/**
	 * Return Application instance
	 * @return
	 * @throws Exception
	 */
	public static Application getApplication() {
		return application;
	}
	
	/**
	 * Returns Application context
	 * @return
	 */
	public static ApplicationContext getApplicationContext() {
		return application.getContext();
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
	 * Creates Log
	 * @param application
	 * @throws Exception
	 */
	protected static void initiateLog() throws Exception {
		XPathReader xmlReader = new XPathReader(CONFIGURE_FILE);
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
	 * Creates Web Server instance.
	 * @param context
	 * @throws Exception
	 */
	protected static void initiateWebServer() throws Exception {

		XPathReader xmlReader = new XPathReader(CONFIGURE_FILE);
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

			// starts web server adds into context
			webServer.start();
			ApplicationContext context = application.getContext();
			context.addWebServer(id, webServer);
		}
	}
	
	/**
	 * Creates SqlSessionProxyFactory
	 * @param context
	 * @throws Exception
	 */
	protected static void initiateSqlSessionProxyFactory() throws Exception {
		XPathReader xmlReader = new XPathReader(CONFIGURE_FILE);
		NodeList sqlSessionProxyFactoryNodeList = (NodeList) xmlReader.getElement("/application/sqlSessionProxyFactory");
		for(int idx = 1; idx <= sqlSessionProxyFactoryNodeList.getLength(); idx ++ ) {
			String sqlSessionProxyFactoryExpression = String.format("/application/sqlSessionProxyFactory[%d]", idx);
			String id = xmlReader.getTextContent(sqlSessionProxyFactoryExpression + "/@id");
			String configureFile = xmlReader.getTextContent(sqlSessionProxyFactoryExpression + "/configureFile");
			SqlSessionProxyFactory sqlSessionProxyFactory = SqlSessionProxyFactory.getInstance(new File(configureFile), PROPERTIES_FILE);
			ApplicationContext context = application.getContext();
			context.addSqlSessionProxyFactory(id, sqlSessionProxyFactory);
		}
	}


}
