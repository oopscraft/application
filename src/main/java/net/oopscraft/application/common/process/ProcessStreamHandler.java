package net.oopscraft.application.common.process;

public interface ProcessStreamHandler {
	
	/**
	 * Reads standard output stream
	 * @param line
	 */
	public void readLine(String line);
	
}
