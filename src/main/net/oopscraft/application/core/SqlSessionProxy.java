package net.oopscraft.application.core;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

public class SqlSessionProxy { 
	
	private static Logger logger = Logger.getLogger(SqlSessionProxy.class);
	
	SqlSession sqlSession = null; 

	/** 
	 * Constructor
	 * @param sqlSession
	 */
	public SqlSessionProxy(SqlSession sqlSession) { 
		this.sqlSession = sqlSession; 
	} 
	
	/** 
	 * Returns DAO instance 
	 * @param clazz 
	 * @return 
	 */ 
	public <T> T getMapper(Class<T> clazz) { 
		return sqlSession.getMapper(clazz); 
	} 

	/**
	 * Commits transaction.
	 */
	public void commit() { 
		if(!SqlTransactionManager.isTransactional()) {
			sqlSession.commit();
		}
	} 
	
	/**
	 * Roll back transaction.
	 */
	public void rollback() { 
		if(!SqlTransactionManager.isTransactional()) {
			sqlSession.rollback();
		}
	} 
	
	/**
	 * Closes sqlSession
	 */
	public void close() { 
		if(!SqlTransactionManager.isTransactional()) {
			sqlSession.close();
		}
	} 
	
	/**
	 * Override finalize method for releasing resource.
	 */
	public void finalize() throws Throwable { 
		try { 
			close(); 
		}catch(Exception ignore){ 
			logger.warn(ignore.getMessage(), ignore);
		}finally{ 
			super.finalize(); 
		} 
	} 
}

