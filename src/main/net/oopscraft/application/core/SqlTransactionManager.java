package net.oopscraft.application.core;

public class SqlTransactionManager {
	
	private static ThreadLocal<SqlTransaction> currentTransaction = new ThreadLocal<SqlTransaction>();
	
	public static void begin() throws Exception {
		if(currentTransaction.get() != null) {
			throw new Exception("Transaction is Already Started.");
		}
		SqlTransaction transacton = new SqlTransaction();
		currentTransaction.set(transacton);
	}
	
	protected static boolean isTransactional() {
		if(currentTransaction.get() != null) {
			return true;
		}else {
			return false;
		}
	}
	
	protected static SqlTransaction getCurrentTransaction() {
		SqlTransaction transaction =  currentTransaction.get();
		return transaction;
	}
	
	public static void commit() throws Exception {
		getCurrentTransaction().commit();
	}
	
	public static void rollback() throws Exception {
		getCurrentTransaction().rollback();
	}
	
	public static void close() throws Exception {
		getCurrentTransaction().close();
		currentTransaction.remove();
	}
	
}
