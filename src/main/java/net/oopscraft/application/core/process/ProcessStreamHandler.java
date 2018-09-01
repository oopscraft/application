package net.oopscraft.application.core.process;

public interface ProcessStreamHandler {
	
	/**
	 * Reads standard output stream
	 * @param line
	 */
	public void readLine(String line);
	
}
