package net.oopscraft.application.core.webserver;

import java.util.ArrayList;
import java.util.Collection;

public abstract class WebServer {
	
	int port = -1;
	boolean ssl = false;
	String keyStorePath = null;
	String keyStoreType = null;
	String keyStorePass = null;
	Collection<WebServerContext> contexts = new ArrayList<WebServerContext>();
	
	public abstract void start() throws Exception;
	
	public abstract void stop() throws Exception;

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

	public Collection<WebServerContext> getContexts() {
		return contexts;
	}

	public void addContext(WebServerContext context) {
		this.contexts.add(context);
	}
}
