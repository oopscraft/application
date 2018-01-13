package net.oopscraft.application.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class SqlSessionProxyFactory { 
	
	private static final Log LOG = LogFactory.getLog(SqlSessionProxyFactory.class); 
	private static Map<File,SqlSessionProxyFactory> instanceMap = new LinkedHashMap<File,SqlSessionProxyFactory>(); 

	File configureFile = null; 
	File propertiesFile = null; 
	String defaultEnvironment = null;
	Map<String,SqlSessionFactory> sqlSessionFactoryMap = new LinkedHashMap<String,SqlSessionFactory>(); 
	
	/**
	 * Initializes SqlSessionProxyFactory instance as ID
	 * @param id
	 * @param configureFile
	 * @param propertiesFile
	 * @throws Exception
	 */
	public synchronized static SqlSessionProxyFactory getInstance(File configureFile ,File propertiesFile) throws Exception {
		synchronized(SqlSessionProxyFactory.class) {
			if(!instanceMap.containsKey(configureFile)){
				SqlSessionProxyFactory sqlSessionProxyFactory = new SqlSessionProxyFactory(configureFile, propertiesFile);
				instanceMap.put(configureFile, sqlSessionProxyFactory);
			}
			return instanceMap.get(configureFile);
		}
	}

	/**
	 * constructor
	 * @param environment
	 * @throws Exception
	 */
	private SqlSessionProxyFactory(File configureFile ,File propertiesFile) throws Exception {
		this.configureFile = configureFile;
		this.propertiesFile = propertiesFile;
		SqlSessionFactory sqlSessionFactory = buildSqlSessionFactory(null);
		defaultEnvironment = sqlSessionFactory.getConfiguration().getEnvironment().getId();
		sqlSessionFactoryMap.put(defaultEnvironment, sqlSessionFactory);
	} 
	
	/**
	 * return default SqlSession.
	 * @return
	 * @throws Exception
	 */
	public synchronized SqlSessionProxy openSession() throws Exception {
		synchronized(this){	
			return openSession(defaultEnvironment);
		}
	}
	
	/**
	 * return Specified SqlSession. 
	 * @param environment
	 * @return
	 * @throws Exception
	 */
	public synchronized SqlSessionProxy openSession(String environment) throws Exception {
		synchronized(this) {
			
	 		// Checking Transaction
	 		if(TransactionManager.isTransactional()) {
	 			if(TransactionManager.getCurrentTransaction().hasSqlSessionProxy(environment)) {
	 				return TransactionManager.getCurrentTransaction().getSqlSessionProxy(environment);
	 			}
	 		}
			
	 		// Checking sqlSessionProxy instance.
			if(!sqlSessionFactoryMap.containsKey(environment)) {
				SqlSessionFactory sqlSessionFactory = buildSqlSessionFactory(environment);
				sqlSessionFactoryMap.put(environment, sqlSessionFactory);
			}
			
	 		// Returns sqlSessionProxy
	 		SqlSession sqlSession = sqlSessionFactoryMap.get(environment).openSession();
	 		SqlSessionProxy sqlSessionProxy = new SqlSessionProxy(sqlSession); 
	 		if(TransactionManager.isTransactional()) {
	 			TransactionManager.getCurrentTransaction().setSqlSessionProxy(environment, sqlSessionProxy);
	 		}
			return sqlSessionProxy;
		}
	}
 	
	/**
	 * Builds SqlSessionFactory by environment.
	 * @param environment
	 * @return
	 * @throws Exception
	 */
 	private SqlSessionFactory buildSqlSessionFactory(String environment) throws Exception {
 		SqlSessionFactory sqlSessionFactory = null;
		InputStream configureInputStream = null;
		InputStream propertiesInputStream = null; 
		try { 
			// create stream 
			configureInputStream = new FileInputStream(configureFile); 
			propertiesInputStream = new FileInputStream(propertiesFile); 
			
			// replace properties value. 
			Properties properties = new Properties();
			properties.load(propertiesInputStream); 
			for(Entry<Object, Object> entry : properties.entrySet()) { 
				Object key = entry.getKey(); 
				Object value = PbeStringEncryptor.decryptIdentifiedValue((String)entry.getValue()); 
				properties.put(key, value); 
			}
			
			// creates sqlSessionProxy
			if(environment != null) {
				sqlSessionFactory = new SqlSessionFactoryBuilder().build(configureInputStream, environment, properties);
			}else {
				sqlSessionFactory = new SqlSessionFactoryBuilder().build(configureInputStream, properties);
			}
		}catch(Exception e){ 
			LOG.error(e.getMessage(),e); 
			throw e; 
		}finally{ 
			if(configureInputStream != null) {
				configureInputStream.close(); 
			}
			if(propertiesInputStream != null) { 
				propertiesInputStream.close(); 
			}
		}
		return sqlSessionFactory;
 	}

 	protected DataSource getDataSource(String environment) throws Exception {
 		SqlSessionFactory sqlSessionFactory = sqlSessionFactoryMap.get(environment);
 		return sqlSessionFactory.getConfiguration().getEnvironment().getDataSource();
 	}
}


