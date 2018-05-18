package net.oopscraft.application;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.oopscraft.application.core.WebServer;

public class ApplicationContainer {

	private static final Log LOG = LogFactory.getLog(ApplicationContainer.class);
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
			// creates ApplicationContext	
			ApplicationContext.initialize(configureFile, propertiesFile);
			applicationContext = ApplicationContext.getInstance();
			
			// starts web server
			for(WebServer webServer : applicationContext.getWebServers().values()) {
				webServer.start();
			}
			
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

}
