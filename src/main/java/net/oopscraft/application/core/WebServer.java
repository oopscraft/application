package net.oopscraft.application.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;

public class WebServer {

	Tomcat tomcat = null;
	int port = -1;
	boolean ssl = false;
	String keyStorePath = null;
	String keyStoreType = null;
	String keyStorePass = null;
	List<WebServerContext> contextList = new ArrayList<WebServerContext>();

	public void addContext(WebServerContext context) throws Exception { 
		contextList.add(context); 
	}

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
		 for(WebServerContext context : this.contextList){ 
			 File resourceBase = new File(context.getResourceBase());
			 StandardContext ctx = (StandardContext)tomcat.addWebapp(context.getContextPath(), resourceBase.getAbsolutePath());
//			 ctx.addParameter("webAppRootKey", Long.toString(System.currentTimeMillis()));
			 ctx.addParameter("webAppRootKey", UUID.randomUUID().toString());
			 ctx.setAltDDName(context.getDescriptor());
			 ctx.setReloadable(false);
			 
			 // add parameter 
			 for(String name : context.getParameters().keySet()){ 
				 String value = context.getParameter(name);
				 ctx.addParameter(name, value);
			 } 
		 } 
		 
		 // starts server instance. 
		 this.tomcat.start();
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

}
