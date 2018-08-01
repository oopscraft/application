public class DatabaseIdProvider implements org.apache.ibatis.mapping.DatabaseIdProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseIdProvider.class);
	
	enum DatabaseId { ORACLE, MYSQL, MSSQL, TIBERO, POSTGRES };
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

}
