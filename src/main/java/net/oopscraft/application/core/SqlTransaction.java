package net.oopscraft.application.core;

import java.util.LinkedHashMap;
import java.util.Map;

public class SqlTransaction {
	
	Map<String,SqlSessionProxy> sqlSessionProxyMap = new LinkedHashMap<String,SqlSessionProxy>();
	
	protected boolean hasSqlSessionProxy(String environment) {
		return sqlSessionProxyMap.containsKey(environment);
	}
	
	
	protected void setSqlSessionProxy(String environment, SqlSessionProxy sqlSessionProxy) {
		sqlSessionProxyMap.put(environment, sqlSessionProxy);
	}
	
	protected SqlSessionProxy getSqlSessionProxy(String environment) throws Exception {
		return sqlSessionProxyMap.get(environment);
	}

	protected void commit() throws Exception {
		for(SqlSessionProxy sqlSessionProxy : sqlSessionProxyMap.values()) {
			try {
				sqlSessionProxy.sqlSession.commit();
			}catch(Exception ignore) {}
		}
	}
	
	protected void rollback() throws Exception {
		for(SqlSessionProxy sqlSessionProxy : sqlSessionProxyMap.values()) {
			try {
				sqlSessionProxy.sqlSession.rollback();
			}catch(Exception ignore) {}
		}
	}
	
	protected void close() throws Exception {
		for(SqlSessionProxy sqlSessionProxy : sqlSessionProxyMap.values()) {
			try {
				sqlSessionProxy.sqlSession.close();
			}catch(Exception ignore) {}
		}
		sqlSessionProxyMap.clear();
	}
}
