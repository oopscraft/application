package net.oopscraft.application.core;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSourceFactory;

public class DbcpDataSourceFactory extends UnpooledDataSourceFactory {
	
	/**
	 * Constructor
	 */
	public DbcpDataSourceFactory() {
		this.dataSource = new BasicDataSource();
	}
	
}
