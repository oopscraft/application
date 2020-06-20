package net.oopscraft.application.core.webserver;

public class WebServerBuilder {
	
	WebServer webServer;

	public WebServerBuilder(Type type) {
		switch(type) {
		case TOMCAT :
			this.webServer = new TomcatWebServer();
		break;
		case JETTY :
			this.webServer = new JettyWebServer();
		break;
		}
	}
	
	public WebServerBuilder setPort(int port) {
		webServer.setPort(port);
		return this;
	}

	public WebServerBuilder setSsl(boolean ssl) {
		webServer.setSsl(ssl);
		return this;
	}

	public WebServerBuilder setKeyStorePath(String keyStorePath) {
		webServer.setKeyStorePath(keyStorePath);
		return this;
	}

	public WebServerBuilder setKeyStoreType(String keyStoreType) {
		webServer.setKeyStoreType(keyStoreType);
		return this;
	}

	public WebServerBuilder setKeyStorePass(String keyStorePass) {
		webServer.setKeyStorePass(keyStorePass);
		return this;
	}

	public WebServerBuilder addContext(WebServerContext context) {
		webServer.addContext(context);
		return this;
	}
	
	public WebServer build() throws Exception {
		return webServer;
	}

}
