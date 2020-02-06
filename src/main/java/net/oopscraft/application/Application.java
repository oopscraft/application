package net.oopscraft.application;

import java.util.Properties;

import org.apache.logging.log4j.core.config.Configurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import net.oopscraft.application.core.webserver.WebServer;
import net.oopscraft.application.core.webserver.WebServerBuilder;
import net.oopscraft.application.core.webserver.WebServerContext;

/**
 * Application Context Configuration
 * @version 0.0.1
 */
public class Application {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(Application.class);
	public static AnnotationConfigApplicationContext applicationContext = null;
	public static Properties properties = null;
	public static WebServer webServer = null;
	
	public static void main(String[] args) throws Exception {

		// setting log4j2 configuration path
		Configurator.initialize(null, "conf/log4j2.xml");
		
		// loads application context
		applicationContext = new AnnotationConfigApplicationContext();
		applicationContext.register(ApplicationContext.class);
		applicationContext.refresh();

		// prints all beans
		for(String name : applicationContext.getBeanDefinitionNames()) {
			LOGGER.info("Bean:{}", name);
		}
		properties = ApplicationContext.properties;
		
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
					webServer.stop();
				}catch(Exception e){
					printError(e.getMessage(), e);
				}
			}
		}));
		
		// creates web server
		WebServerBuilder webServerBuilder = new WebServerBuilder(WebServerBuilder.Type.valueOf((String)properties.get("application.webServer.type")));
		webServerBuilder.setPort(Integer.parseInt((String)properties.get("application.webServer.port")));
		WebServerContext context = new WebServerContext();
		context.setContextPath((String)properties.get("application.webServer.context.contextPath"));
		context.setResourceBase((String)properties.get("application.webServer.context.ResourceBase"));
		webServerBuilder.addContext(context);
		webServer = webServerBuilder.build();
		
		// starts web server
		webServer.start();
		
		// join main thread
		Thread.currentThread().join();
	}
	
	/**
	 * printInfo
	 * @param message
	 */
	private static void printInfo(Object message) {
		LOGGER.warn("{}", message);
		System.err.println(message);
	}
	
	/**
	 * printInfo
	 * @param message
	 * @param e
	 */
	private static void printError(String message, Exception e) {
		LOGGER.error(e.getMessage(), e);
		e.printStackTrace(System.err);
	}

}
