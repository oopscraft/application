package net.oopscraft.application;

import java.io.File;
import java.sql.Connection;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.sql.DataSource;

import net.oopscraft.application.core.WebServer;

public class Application {
	
	File contextXmlFile;
	File contextPropertiesFile;
	HashMap<String,WebServer> webServers = new LinkedHashMap<String,WebServer>();
	HashMap<String,DataSource> dataSources = new LinkedHashMap<String,DataSource>();
	
	final void setContextXmlFile(File contextXmlFile) throws Exception {
		this.contextXmlFile = contextXmlFile;
	}
	
	final void setContextPropertiesFile(File contextPropertiesFile) {
		this.contextPropertiesFile = contextPropertiesFile;
	}
	
	final void setWebServers(HashMap<String,WebServer> webServers) {
		this.webServers = webServers;
	}
	
	final void setWebServer(String id, WebServer webServer) {
		webServers.put(id, webServer);
	}

	public final HashMap<String,WebServer> getWebServers() {
		return webServers;
	}
	
	public final WebServer getWebServer(String id) {
		return webServers.get(id);
	}

	final void setDataSource(String id, DataSource dataSource) {
		dataSources.put(id, dataSource);
	}
	
	public final void setDataSources(HashMap<String,DataSource> dataSources) {
		this.dataSources = dataSources;
	}
	
	public final HashMap<String,DataSource> getDataSources() {
		return dataSources;
	}
	
	public final DataSource getDataSource(String id) {
		return dataSources.get(id);
	}
	
	public final void start() throws Exception {
		
		Connection connection = this.getDataSource("oltp").getConnection();
		System.err.println(connection);
		
		onStart();
	}
	
	public final void stop() throws Exception {
		onStop();
	}
	
	/**
	 * process on application start
	 * @throws Exception
	 */
	public void onStart() throws Exception {
		// void
	};
	
	/**
	 * process on application stop
	 * @throws Exception
	 */
	public void onStop() throws Exception {
		// void
	};


}
