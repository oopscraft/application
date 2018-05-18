package net.oopscraft.application.core;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;

import net.oopscraft.application.ApplicationContext;

public class DataSourceProxy implements DataSource {
	
	private String id;
	private String environment;
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return null;
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		return 0;
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return null;
	}

	@Override
	public void setLogWriter(PrintWriter arg0) throws SQLException {
	}

	@Override
	public void setLoginTimeout(int arg0) throws SQLException {
	}

	@Override
	public boolean isWrapperFor(Class<?> arg0) throws SQLException {
		return false;
	}

	@Override
	public <T> T unwrap(Class<T> arg0) throws SQLException {
		return null;
	}

	@Override
	public Connection getConnection() throws SQLException {
		Connection connection = null;
		try {
			ApplicationContext applicationContext = ApplicationContext.getInstance();
			SqlSessionProxyFactory sqlSessionProxyFactory = applicationContext.getSqlSessionProxyFactory(id);
			DataSource dataSource = sqlSessionProxyFactory.getDataSource(environment);
			connection = dataSource.getConnection();
		}catch(Exception e) {
			throw new SQLException(e);
		}
		return connection;
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		return null;
	}

}
