package net.oopscraft.application.core.webserver;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.commons.io.FileUtils;
import org.apache.tomcat.JarScanFilter;
import org.apache.tomcat.JarScanType;
import org.apache.tomcat.util.scan.StandardJarScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebServer {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WebServer.class);

	Tomcat tomcat = null;
	int port = -1;
	boolean ssl = false;
	String keyStorePath = null;
	String keyStoreType = null;
	String keyStorePass = null;
	Collection<WebServerContext> contexts = new ArrayList<WebServerContext>();

	public void start() throws Exception {
		 
		this.tomcat = new Tomcat();
		this.tomcat.setPort(this.port);

		 // setting base directory 
		File baseDir = new File(".tomcat" + File.separator + this.port);
		baseDir.mkdirs();
		this.tomcat.setBaseDir(baseDir.getAbsolutePath());

		// setting connector 
		Connector connector = this.tomcat.getConnector();
		if(this.ssl) { 
			connector.setSecure(true);
			connector.setScheme("https");
			connector.setAttribute("SSLEnabled", true);
			connector.setAttribute("protocol", "HTTP/1.1");
			connector.setAttribute("sslProtocol", "TLS");
			connector.setAttribute("clientAuth", false);
			connector.setAttribute("keystoreFile", new File(this.keyStorePath).getAbsolutePath());
			connector.setAttribute("keystoreType", this.keyStoreType);
			connector.setAttribute("keystorePass", this.keyStorePass);
		}
		 
		// setting context 
		for(WebServerContext context : this.contexts){
			File resourceBase = new File(context.getResourceBase());
			StandardContext ctx = (StandardContext)tomcat.addWebapp(context.getContextPath(), resourceBase.getAbsolutePath());
			ctx.addParameter("webAppRootKey", UUID.randomUUID().toString());
			ctx.setDefaultWebXml(context.getDescriptor());
			ctx.setReloadable(true);
			ctx.setParentClassLoader(Thread.currentThread().getContextClassLoader());
			
			// sets jar scanner
			ctx.setXmlBlockExternal(false);
			StandardJarScanner jarScanner = new StandardJarScanner();
			jarScanner.setScanClassPath(true);
			jarScanner.setJarScanFilter(new JarScanFilter() {
				@Override
				public boolean check(JarScanType jarScanType, String jarName) {
					LOGGER.debug(jarName);
					return true;
				}
			});
			ctx.setJarScanner(jarScanner);
			
			
			ClassLoader loadClass = Thread.currentThread().getContextClassLoader() ; 
			InputStream is = loadClass.getResourceAsStream("META-INF/web-fragment.xml");
			File targetFile = new File("webapp/application/META-INF/web-fragment.xml");
			FileUtils.copyInputStreamToFile(is, targetFile);
			LOGGER.warn(targetFile.getAbsolutePath());
			ctx.setAltDDName(targetFile.getAbsolutePath());
			
			

			

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
	
	public void stop() throws Exception {
		 this.tomcat.stop();
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public boolean isSsl() {
		return ssl;
	}

	public void setSsl(boolean ssl) {
		this.ssl = ssl;
	}

	public String getKeyStorePath() {
		return keyStorePath;
	}

	public void setKeyStorePath(String keyStorePath) {
		this.keyStorePath = keyStorePath;
	}

	public String getKeyStoreType() {
		return keyStoreType;
	}

	public void setKeyStoreType(String keyStoreType) {
		this.keyStoreType = keyStoreType;
	}

	public String getKeyStorePass() {
		return keyStorePass;
	}

	public void setKeyStorePass(String keyStorePass) {
		this.keyStorePass = keyStorePass;
	}
	
	public void addContext(WebServerContext context) throws Exception { 
		contexts.add(context); 
	}
	
	public void setContexts(Collection<WebServerContext> contexts) {
		this.contexts = contexts;
	}

}
