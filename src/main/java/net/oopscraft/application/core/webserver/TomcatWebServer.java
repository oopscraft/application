package net.oopscraft.application.core.webserver;

import java.io.File;
import java.net.URL;
import java.util.Deque;
import java.util.Set;
import java.util.UUID;

import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.JarScanFilter;
import org.apache.tomcat.JarScanType;
import org.apache.tomcat.JarScannerCallback;
import org.apache.tomcat.util.scan.StandardJarScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TomcatWebServer extends WebServer {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TomcatWebServer.class);
	Tomcat tomcat = null;

	@Override
	public void start() throws Exception {
		 
		tomcat = new Tomcat();
		tomcat.setPort(this.getPort());

		 // setting base directory 
		File baseDir = new File(".tomcat" + File.separator + this.getPort());
		baseDir.mkdirs();
		tomcat.setBaseDir(baseDir.getAbsolutePath());

		// setting connector 
		Connector connector = this.tomcat.getConnector();
		if(this.isSsl()) { 
			connector.setSecure(true);
			connector.setScheme("https");
			connector.setAttribute("SSLEnabled", true);
			connector.setAttribute("protocol", "HTTP/1.1");
			connector.setAttribute("sslProtocol", "TLS");
			connector.setAttribute("clientAuth", false);
			connector.setAttribute("keystoreFile", new File(this.getKeyStorePath()).getAbsolutePath());
			connector.setAttribute("keystoreType", this.getKeyStoreType());
			connector.setAttribute("keystorePass", this.getKeyStorePass());
		}
		 
		// setting context 
		for(WebServerContext context : this.getContexts()){
			File resourceBase = new File(context.getResourceBase());
			StandardContext ctx = (StandardContext)tomcat.addWebapp(context.getContextPath(), resourceBase.getAbsolutePath());
			ctx.addParameter("webAppRootKey", UUID.randomUUID().toString());
			if(context.getDescriptor() != null && context.getDescriptor().trim().length() > 0) {
				ctx.setAltDDName(context.getDescriptor());
			}
			ctx.setReloadable(true);
			ctx.setParentClassLoader(Thread.currentThread().getContextClassLoader());
			
			// sets jar scanner
			ctx.setXmlBlockExternal(false);
			StandardJarScanner jarScanner = new StandardJarScanner() {
				/**
				 * override to load web-fragment.xml in all class path jar(set isWebapp as true)
				 */
				@Override
			    protected void processURLs(JarScanType scanType, JarScannerCallback callback, Set<URL> processedURLs, boolean isWebapp, Deque<URL> classPathUrlsToProcess) {
					super.processURLs(scanType, callback, processedURLs, true, classPathUrlsToProcess);
				}
			};
			jarScanner.setScanClassPath(true);
			// for debugging library
			jarScanner.setJarScanFilter(new JarScanFilter() {
				@Override
				public boolean check(JarScanType jarScanType, String jarName) {
					LOGGER.debug(jarName);
					return true;
				}
			});
			ctx.setJarScanner(jarScanner);
	        
			// add parameter 
			if(context.getParameter() != null) {
				for(String name : context.getParameter().keySet()){ 
					String value = context.getParameter(name);
					ctx.addParameter(name, value);
				}
			}
		} 
		
		// starts server instance. 
		tomcat.start();
	}
	
	@Override
	public void stop() throws Exception {
		 this.tomcat.stop();
	}

}
