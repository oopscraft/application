package net.oopscraft.application;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import net.oopscraft.application.core.SqlSessionProxyFactory;
import net.oopscraft.application.core.WebServer;

public class ApplicationContext {
	

	private static ApplicationContext instance;
	
	protected File configureFile;
	protected File propertiesFile;
	protected Map<String,WebServer> webServers = new LinkedHashMap<String,WebServer>();
	protected Map<String,SqlSessionProxyFactory> sqlSessionProxyFactories = new LinkedHashMap<String,SqlSessionProxyFactory>();
	
	public synchronized static void initialize(File configureFile, File propertiesFile) throws Exception {
		synchronized(ApplicationContext.class) {
			if(instance == null) {
				instance = new ApplicationContext(configureFile, propertiesFile);
			}
		}
	}
	
	/**
	 * getInstance
	 * @return
	 * @throws Exception
	 */
	public synchronized static ApplicationContext getInstance() throws Exception {
		synchronized(ApplicationContext.class) {
			if(instance == null) {
				throw new Exception("ApplicationContext is not initialized.");
			}
		}
		return instance;
	}
	
	/**
	 * constructor
	 * @throws Exception
	 */
	private ApplicationContext(File configureFile, File propertiesFile) throws Exception {
		this.configureFile = configureFile;
		this.propertiesFile = propertiesFile;
	}
	

	public Map<String,WebServer> getWebServers() {
		return webServers;
	}
	
	public WebServer getWebServer(String id) throws Exception {
		if(!webServers.containsKey(id)) {
			throw new Exception(String.format("WebServer[%s] is invalid.", id));
		}
		return webServers.get(id);
	}
	
	public Map<String,SqlSessionProxyFactory> getSqlSessionProxyFactories() {
		return sqlSessionProxyFactories;
	}
	
	public SqlSessionProxyFactory getSqlSessionProxyFactory(String id) throws Exception {
		if(!sqlSessionProxyFactories.containsKey(id)) {
			throw new Exception(String.format("SqlSessionProxyFactory[%s] is invalid.", id));
		}
		return sqlSessionProxyFactories.get(id);
	}
	
}
