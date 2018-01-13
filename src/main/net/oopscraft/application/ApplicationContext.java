package net.oopscraft.application;

import java.util.LinkedHashMap;
import java.util.Map;

import net.oopscraft.application.core.SqlSessionProxyFactory;
import net.oopscraft.application.core.WebServer;

public class ApplicationContext {
	
	protected String[] arguments;
	protected Map<String,WebServer> webServerMap = new LinkedHashMap<String,WebServer>();
	protected Map<String,SqlSessionProxyFactory> sqlSessionProxyFactoryMap = new LinkedHashMap<String,SqlSessionProxyFactory>();

	/**
	 * Constructor
	 * @param args
	 */
	public ApplicationContext(String[] arguments) {
		this.arguments = arguments;
	}
	
	/**
	 * getArguments
	 * @return
	 */
	public String[] getArguments() {
		return this.arguments;
	}
	
	/**
	 * addWebServer
	 * @param id
	 * @param webServer
	 * @throws Exception
	 */
	protected void addWebServer(String id, WebServer webServer) throws Exception {
		if(webServerMap.containsKey(id)){
			 throw new Exception(String.format("WebServer[id:%s] is already exists.", id));
		}
		webServerMap.put(id, webServer);
	}

	/**
	 * getWebServer
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public WebServer getWebServer(String id) throws Exception {
		if(webServerMap.containsKey(id)){
			return webServerMap.get(id);
		}
		throw new Exception(String.format("WebServer[id:%s] is invalid.", id));
	}
	
	/**
	 * Setting SqlSessionProxyFactory
	 * @param sqlSessionFactory
	 */
	protected void addSqlSessionProxyFactory(String id, SqlSessionProxyFactory sqlSessionProxyFactory) {
		sqlSessionProxyFactoryMap.put(id, sqlSessionProxyFactory);
	}

	/**
	 * getSqlSessionProxyFactory
	 * @return
	 */
	public SqlSessionProxyFactory getSqlSessionProxyFactory(String id) throws Exception {
		return sqlSessionProxyFactoryMap.get(id);
	}
	
}
