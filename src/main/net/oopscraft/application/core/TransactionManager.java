package net.oopscraft.application.core;

public class TransactionManager {
	
	private static ThreadLocal<Transaction> currentTransaction = new ThreadLocal<Transaction>();
	
	public static void begin() throws Exception {
		if(currentTransaction.get() != null) {
			throw new Exception("Transaction is Already Started.");
		}
		Transaction transacton = new Transaction();
		currentTransaction.set(transacton);
	}
	
	protected static boolean isTransactional() {
		if(currentTransaction.get() != null) {
			return true;
		}else {
			return false;
		}
	}
	
	protected static Transaction getCurrentTransaction() {
		Transaction transaction =  currentTransaction.get();
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
