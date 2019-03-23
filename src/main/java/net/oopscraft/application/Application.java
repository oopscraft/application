package net.oopscraft.application;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import net.oopscraft.application.core.webserver.WebServer;
import net.oopscraft.application.core.webserver.WebServerContext;

/**
 * Application Context Configuration
 * @author chomookun@gmail.com
 * @version 0.0.1
 * @see    None
 */
public class Application {
	
	public static AnnotationConfigApplicationContext context = null;
	public static WebServer webServer = null;
	
	public static void main(String[] args) throws Exception {
		
		context = new AnnotationConfigApplicationContext(ApplicationContext.class);
		
		for(String name : context.getBeanDefinitionNames()) {
			System.out.println(name);
		}
		
		// hooking kill signal
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable(){
			public void run() {
				try {
					context.close();
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
		
		// wait
		Thread.currentThread().join();
	}

}
