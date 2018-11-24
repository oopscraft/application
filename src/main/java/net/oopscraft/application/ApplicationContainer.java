package net.oopscraft.application;

import java.io.File;

import org.apache.logging.log4j.core.config.Configurator;
import org.jasypt.contrib.org.apache.commons.codec_1_3.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.oopscraft.application.core.XPathReader;
import net.oopscraft.application.core.webserver.WebServer;

public class ApplicationContainer {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationContainer.class);
	private static final String BANNER = new String(Base64.decodeBase64("ICBfX19fICBfX19fICBfX18gIF9fX19fX19fX19fXyAgX19fICAgX19fX19fX19fXyAgICBfX18gICBfX18gIF9fXyAgX18gICBfX19fX19fX19fX18gX19fX19fX19fX19fX18gIF8gIF9fDQogLyBfXyBcLyBfXyBcLyBfIFwvIF9fLyBfX18vIF8gXC8gXyB8IC8gX18vXyAgX18vX19fLyBfIHwgLyBfIFwvIF8gXC8gLyAgLyAgXy8gX19fLyBfIC9fICBfXy8gIF8vIF9fIFwvIHwvIC8NCi8gL18vIC8gL18vIC8gX19fL1wgXC8gL19fLyAsIF8vIF9fIHwvIF8vICAvIC8gL19fXy8gX18gfC8gX19fLyBfX18vIC9fX18vIC8vIC9fXy8gX18gfC8gLyBfLyAvLyAvXy8gLyAgICAvIA0KXF9fX18vXF9fX18vXy8gIC9fX18vXF9fXy9fL3xfL18vIHxfL18vICAgL18vICAgICAvXy8gfF8vXy8gIC9fLyAgL19fX18vX19fL1xfX18vXy8gfF8vXy8gL19fXy9cX19fXy9fL3xfLyAgDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA=".getBytes()));
	private static final File APPLICATION_XML = new File("conf/application.xml");
	private static final File APPLICATION_PROPERTIES = new File("conf/application.properties");
	
	private static Application application;
	
	/**
	 * main
	 * @param args
	 * @throws Exception
	 */
	public static void main(final String[] args) throws Exception {
		
		// setting log4j2 configuration path
		Configurator.initialize(null, "conf/log4j2.xml");
		
		// prints banner
		LOGGER.info(System.lineSeparator() + BANNER);
		
		// initializes application
		application = getApplication();
		
		// hooking kill signal
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable(){
			public void run() {
				try {
					application.stop();
				}catch(Exception e){
					e.printStackTrace(System.err);
					LOGGER.warn(e.getMessage(), e);
				}
			}
		}));
		
		// starts webServer
		for(WebServer webServer : application.getWebServers().values()) {
			webServer.start();
		}
		
		// thread join
		Thread.currentThread().join();
	}
	
	public synchronized static Application getApplication() throws Exception {
		synchronized(ApplicationContainer.class) {
			if(application == null) {
				// initializes application
				XPathReader applicationXmlReader = new XPathReader(APPLICATION_XML);
				String applicationClassName = applicationXmlReader.getTextContent("/application/@class");
				Class<?> applicationClass = Class.forName(applicationClassName);
				application = new ApplicationBuilder()
						.setClass(applicationClass)
						.setXmlFile(APPLICATION_XML)
						.setPropertiesFile(APPLICATION_PROPERTIES)
						.build();
				application.start();
			}
			return application;
		}
	}

}
