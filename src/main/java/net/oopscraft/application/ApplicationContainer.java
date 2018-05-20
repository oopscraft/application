package net.oopscraft.application;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.logging.log4j.core.config.Configurator;
import org.jasypt.contrib.org.apache.commons.codec_1_3.binary.Base64;
import org.w3c.dom.NodeList;

import net.oopscraft.application.core.SqlSessionProxyFactory;
import net.oopscraft.application.core.WebServer;
import net.oopscraft.application.core.WebServerContext;
import net.oopscraft.application.core.XPathReader;

public class ApplicationContainer {

	private static final Log LOG = LogFactory.getLog(ApplicationContainer.class);
	private static final String WELCOME_MESSAGE = new String(Base64.decodeBase64("ICBfX19fICBfX19fICBfX18gIF9fX19fX19fX19fXyAgX19fICAgX19fX19fX19fXyAgICBfX18gICBfX18gIF9fXyAgX18gICBfX19fX19fX19fX18gX19fX19fX19fX19fX18gIF8gIF9fDQogLyBfXyBcLyBfXyBcLyBfIFwvIF9fLyBfX18vIF8gXC8gXyB8IC8gX18vXyAgX18vX19fLyBfIHwgLyBfIFwvIF8gXC8gLyAgLyAgXy8gX19fLyBfIC9fICBfXy8gIF8vIF9fIFwvIHwvIC8NCi8gL18vIC8gL18vIC8gX19fL1wgXC8gL19fLyAsIF8vIF9fIHwvIF8vICAvIC8gL19fXy8gX18gfC8gX19fLyBfX18vIC9fX18vIC8vIC9fXy8gX18gfC8gLyBfLyAvLyAvXy8gLyAgICAvIA0KXF9fX18vXF9fX18vXy8gIC9fX18vXF9fXy9fL3xfL18vIHxfL18vICAgL18vICAgICAvXy8gfF8vXy8gIC9fLyAgL19fX18vX19fL1xfX18vXy8gfF8vXy8gL19fXy9cX19fXy9fL3xfLyAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA=".getBytes()));
	private static ApplicationContext applicationContext;
	private static Application application;
	
	/**
	 * launch
	 * @param application
	 * @param args
	 * @throws Exception
	 */
	public static void launch(Class<? extends Application> applicationClass, File configureFile, File propertiesFile) {
		try{
			// loading log4j2 configuration file.
			Configurator.initialize(null, "conf/log4j2.xml");
			
			// creates ApplicationContext	
			ApplicationContext.initialize(configureFile, propertiesFile);
			applicationContext = ApplicationContext.getInstance();
			
			// create resource
			printWelcomeMessage();
			createSqlSessionProxyFactories();
			createWebServers();
			
			// start application
			application = applicationClass.newInstance();
			application.onStart();
			
			// hooking kill signal
			Runtime.getRuntime().addShutdownHook(new Thread(new Runnable(){
				public void run() {
					// stop application
					try {
						application.onStop();
					}catch(Exception e){
						e.printStackTrace(System.err);
						LOG.warn(e.getMessage(), e);
					}
					// starts web server
					for(WebServer webServer : applicationContext.getWebServers().values()) {
						try {
							webServer.stop();
						}catch(Exception e) {
							e.printStackTrace(System.err);
							LOG.warn(e.getMessage(), e);
						}
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
	
	/**
	 * printWelcomeMessage
	 * @throws Exception
	 */
	private static void printWelcomeMessage() throws Exception {
		LOG.info(System.lineSeparator() + WELCOME_MESSAGE);
	}
	
	
	/**
	 * Creates Web Server instance.
	 * @param context
	 * @throws Exception
	 */
	private static void createWebServers() throws Exception {
		XPathReader xmlReader = new XPathReader(applicationContext.configureFile);
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

			// start server
			webServer.start();
			
			// add webServer
			applicationContext.webServers.put(id, webServer);
		}
	}
	
	/**
	 * Creates SqlSessionProxyFactory
	 * @param context
	 * @throws Exception
	 */
	private static void createSqlSessionProxyFactories() throws Exception {
		XPathReader xmlReader = new XPathReader(applicationContext.configureFile);
		NodeList sqlSessionProxyFactoryNodeList = (NodeList) xmlReader.getElement("/application/sqlSessionProxyFactory");
		for(int idx = 1; idx <= sqlSessionProxyFactoryNodeList.getLength(); idx ++ ) {
			String sqlSessionProxyFactoryExpression = String.format("/application/sqlSessionProxyFactory[%d]", idx);
			String id = xmlReader.getTextContent(sqlSessionProxyFactoryExpression + "/@id");
			String configureFile = xmlReader.getTextContent(sqlSessionProxyFactoryExpression + "/configureFile");
			SqlSessionProxyFactory sqlSessionProxyFactory = SqlSessionProxyFactory.getInstance(new File(configureFile), applicationContext.propertiesFile);
			applicationContext.sqlSessionProxyFactories.put(id, sqlSessionProxyFactory);
		}
	}

}
