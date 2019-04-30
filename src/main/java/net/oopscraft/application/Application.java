package net.oopscraft.application;

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
		
		applicationContext = new AnnotationConfigApplicationContext(ApplicationContext.class);
		
		for(String name : applicationContext.getBeanDefinitionNames()) {
			LOGGER.info(name);
		}
		
		// hooking kill signal
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable(){
			public void run() {
				try {
					applicationContext.close();
				}catch(Exception e){
					e.printStackTrace(System.err);
				}
				try {
					webServer.stop();
				}catch(Exception e) {
					e.printStackTrace(System.err);
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

}
