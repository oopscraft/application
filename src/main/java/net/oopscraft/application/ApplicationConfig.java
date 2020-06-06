package net.oopscraft.application;

import net.oopscraft.application.core.webserver.Type;
import net.oopscraft.application.security.SecurityPolicy;

public class ApplicationConfig {
	
	SecurityPolicy securityPolicy;
	String theme;
	String locales;
	
	class WebServerConfig {
		Type type;
		int port;
		boolean ssl;
		String keyStorePath;
		String keyStoreType;
		String keyStorePass;
		public Type getType() {
			return type;
		}
		public void setType(Type type) {
			this.type = type;
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
	transient WebServerConfig webServer = new WebServerConfig();

	
	class DataSourceConfig {
		String driver;
		String url;
		String username;
		String password;
		int initialSize;
		int maxTotal;
		String validationQuery;
		public String getDriver() {
			return driver;
		}
		public void setDriver(String driver) {
			this.driver = driver;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public int getInitialSize() {
			return initialSize;
		}
		public void setInitialSize(int initialSize) {
			this.initialSize = initialSize;
		}
		public int getMaxTotal() {
			return maxTotal;
		}
		public void setMaxTotal(int maxTotal) {
			this.maxTotal = maxTotal;
		}
		public String getValidationQuery() {
			return validationQuery;
		}
		public void setValidationQuery(String validationQuery) {
			this.validationQuery = validationQuery;
		}
	}
	transient DataSourceConfig dataSource = new DataSourceConfig();
	
	class EntityManagerFactoryConfig {
		String databasePlatform;
		String packagesToScan;
		public String getDatabasePlatform() {
			return databasePlatform;
		}
		public void setDatabasePlatform(String databasePlatform) {
			this.databasePlatform = databasePlatform;
		}
		public String getPackagesToScan() {
			return packagesToScan;
		}
		public void setPackagesToScan(String packagesToScan) {
			this.packagesToScan = packagesToScan;
		}
	}
	transient EntityManagerFactoryConfig entityManagerFactory = new EntityManagerFactoryConfig();
	
	class SqlSessionFactoryConfig {
		String databaseId;
		String mapperLocations;
		public String getDatabaseId() {
			return databaseId;
		}
		public void setDatabaseId(String databaseId) {
			this.databaseId = databaseId;
		}
		public String getMapperLocations() {
			return mapperLocations;
		}
		public void setMapperLocations(String mapperLocations) {
			this.mapperLocations = mapperLocations;
		}
	}
	transient SqlSessionFactoryConfig sqlSessionFactory = new SqlSessionFactoryConfig();
	
	public SecurityPolicy getSecurityPolicy() {
		return securityPolicy;
	}
	public void setSecurityPolicy(SecurityPolicy securityPolicy) {
		this.securityPolicy = securityPolicy;
	}
	public String getTheme() {
		return theme;
	}
	public void setTheme(String theme) {
		this.theme = theme;
	}
	public String getLocales() {
		return locales;
	}
	public void setLocales(String locales) {
		this.locales = locales;
	}
	public WebServerConfig getWebServer() {
		return webServer;
	}
	public void setWebServer(WebServerConfig webServer) {
		this.webServer = webServer;
	}
	public DataSourceConfig getDataSource() {
		return dataSource;
	}
	public void setDataSource(DataSourceConfig dataSource) {
		this.dataSource = dataSource;
	}
	public EntityManagerFactoryConfig getEntityManagerFactory() {
		return entityManagerFactory;
	}
	public void setEntityManagerFactory(EntityManagerFactoryConfig entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
	}
	public SqlSessionFactoryConfig getSqlSessionFactory() {
		return sqlSessionFactory;
	}
	public void setSqlSessionFactory(SqlSessionFactoryConfig sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}

}
