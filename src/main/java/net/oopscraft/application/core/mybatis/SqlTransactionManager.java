package net.oopscraft.application.core.mybatis;

public class SqlTransactionManager {
	
	private static ThreadLocal<SqlTransaction> currentSqlTransaction = new ThreadLocal<SqlTransaction>();
	
	public static void begin() throws Exception {
		if(currentSqlTransaction.get() != null) {
			throw new Exception("Transaction is Already Started.");
		}
		SqlTransaction sqkTransacton = new SqlTransaction();
		currentSqlTransaction.set(sqkTransacton);
	}
	
	protected static boolean isTransactional() {
		if(currentSqlTransaction.get() != null) {
			return true;
		}else {
			return false;
		}
	}
	
	protected static SqlTransaction getCurrentSqlTransaction() {
		SqlTransaction transaction =  currentSqlTransaction.get();
		return transaction;
	}
	
	public static void commit() throws Exception {
		getCurrentSqlTransaction().commit();
	}
	
	public static void rollback() throws Exception {
		getCurrentSqlTransaction().rollback();
	}
	
	public static void close() throws Exception {
		getCurrentSqlTransaction().close();
		currentSqlTransaction.remove();
	}
	
}
