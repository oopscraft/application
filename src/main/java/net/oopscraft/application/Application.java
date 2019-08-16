package net.oopscraft.application;

import org.apache.logging.log4j.core.config.Configurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import net.oopscraft.application.core.webserver.WebServer;
import net.oopscraft.application.core.webserver.WebServerContext;

/**
 * Application Context Configuration
 * @version 0.0.1
 * @see    None
 */
public class Application {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(Application.class);
	
	public static AnnotationConfigApplicationContext applicationContext = null;
	public static WebServer webServer = null;
	
	public static void main(String[] args) throws Exception {
		
		// setting log4j2 configuration path
		Configurator.initialize(null, "conf/log4j2.xml");

		// loads application context
		applicationContext = new AnnotationConfigApplicationContext(
				 ApplicationContext.class
				//,ApplicationScheduleContext.class
				);
		for(String name : applicationContext.getBeanDefinitionNames()) {
			LOGGER.info("Bean:{}", name);
		}
		
		// hooking kill signal
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable(){
			public void run() {
				printInfo("Runtime.getRuntime().addShutdownHook...");
				try {
					applicationContext.close();
				}catch(Exception e){
					e.printStackTrace(System.err);
				}
				try {
					applicationContext.close();
				}catch(Exception e){
					printError(e.getMessage(), e);
				}
			}
		}));
		
		// creates web server
		WebServer webServer = new WebServer();
		webServer.setPort(10001);
		WebServerContext webServerContext = new WebServerContext();
		webServerContext.setContextPath("");
		webServerContext.setResourceBase("webapp");
		webServer.addContext(webServerContext);
		webServer.start();
		
		// join main thread
		Thread.currentThread().join();
	}
	
	private static void printInfo(Object message) {
		LOGGER.warn("{}", message);
		System.err.println(message);
	}
	
	private static void printError(String messgae, Exception e) {
		LOGGER.error(e.getMessage(), e);
		e.printStackTrace(System.err);
	}

}
