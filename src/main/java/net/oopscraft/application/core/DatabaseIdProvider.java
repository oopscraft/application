package net.oopscraft.application.core;

import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseIdProvider implements org.apache.ibatis.mapping.DatabaseIdProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseIdProvider.class);
	
	protected enum DatabaseId { ANSI, MYSQL, POSTGRES, ORACLE, MSSQL };
	String databaseId;
	
	@Override
	public void setProperties(Properties properties) {
		this.databaseId = (String)properties.get("databaseId");
	}

	@Override
	public String getDatabaseId(DataSource dataSource) throws SQLException {
		if(databaseId != null) {
			try {
				return DatabaseId.valueOf(databaseId).name();
			}catch(Exception e) {
				LOGGER.warn(e.getMessage(),e);
				throw new SQLException("Invalid databaseId[" + databaseId + "]");
			}
		}
		return null;
	}
	
	public void setDatabaseId(String databaseId) {
		this.databaseId = databaseId;
	}

}
