package net.oopscraft.application;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.MessageSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import net.oopscraft.application.core.webserver.WebServer;
import net.oopscraft.application.monitor.MonitorAgent;

public class Application {
	
	File xmlFile;
	File propertiesFile;
	MonitorAgent monitorAgent;
	Map<String,String>config =new LinkedHashMap<String,String>();
	Map<String,WebServer> webServers = new LinkedHashMap<String,WebServer>();
	Map<String,DataSource> dataSources = new LinkedHashMap<String,DataSource>();
	Map<String,LocalContainerEntityManagerFactoryBean> entityManagerFactories = new LinkedHashMap<String,LocalContainerEntityManagerFactoryBean >();
	Map<String,SqlSessionFactoryBean> sqlSessionFactories = new LinkedHashMap<String,SqlSessionFactoryBean>();
	Map<String,MessageSource> messageSources = new LinkedHashMap<String,MessageSource>();
	
	final void setXmlFile(File xmlFile) throws Exception {
		this.xmlFile = xmlFile;
	}
	
	final void setPropertiesFile(File propertiesFile) {
		this.propertiesFile = propertiesFile;
	}

	final void setConfig(String name, String value) {
		config.put(name, value);
	}
	
	final String getConfig(String name) {
		return config.get(name);
	}
	
	final void setMonitorAgent(MonitorAgent monitorAgent) {
		this.monitorAgent = monitorAgent;
	}
	
	public final MonitorAgent getMonitorAgent() {
		return this.monitorAgent;
	}
	
	final void setWebServers(Map<String,WebServer> webServers) {
		this.webServers = webServers;
	}
	
	final void setWebServer(String id, WebServer webServer) {
		webServers.put(id, webServer);
	}

	public final Map<String,WebServer> getWebServers() {
		return webServers;
	}
	
	public final WebServer getWebServer(String id) {
		return webServers.get(id);
	}

	final void setDataSource(String id, DataSource dataSource) {
		dataSources.put(id, dataSource);
	}
	
	public final void setDataSources(Map<String,DataSource> dataSources) {
		this.dataSources = dataSources;
	}
	
	public final Map<String,DataSource> getDataSources() {
		return dataSources;
	}
	
	public final DataSource getDataSource(String id) {
		return dataSources.get(id);
	}

	final void setEntityManagerFactories(Map<String,LocalContainerEntityManagerFactoryBean > entityManagerFactories) {
		this.entityManagerFactories = entityManagerFactories;
	}
	
	final void setEntityManagerFactory(String id, LocalContainerEntityManagerFactoryBean  entityManagerFactory) {
		entityManagerFactories.put(id, entityManagerFactory);
	}
	
	public final LocalContainerEntityManagerFactoryBean getEntityManagerFactory(String id) {
		return entityManagerFactories.get(id);
	}
	
	public final Map<String,LocalContainerEntityManagerFactoryBean > getEntityManagerFactories() {
		return entityManagerFactories;
	}
	
	final void setSqlSessionFactories(Map<String,SqlSessionFactoryBean> sqlSessionFactories) {
		this.sqlSessionFactories = sqlSessionFactories;
	}
	
	final void setSqlSessionFactory(String id, SqlSessionFactoryBean sqlSessionFactory) {
		this.sqlSessionFactories.put(id, sqlSessionFactory);
	}
	
	public final SqlSessionFactoryBean getSqlSessionFactory(String id) {
		return sqlSessionFactories.get(id);
	}
	
	public final Map<String,SqlSessionFactoryBean> getSqlSessionFactories(){
		return sqlSessionFactories;
	}
	
	final void setMessageSources(Map<String,MessageSource> messageSources) {
		this.messageSources = messageSources;
	}
	
	final void setMessageSource(String id, MessageSource messageSource) {
		this.messageSources.put(id, messageSource);
	}
	
	public final MessageSource getMessageSource(String id) {
		return messageSources.get(id);
	}
	
	public final Map<String,MessageSource> getMessageSources(){
		return messageSources;
	}
	
	public final void start() throws Exception {
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
