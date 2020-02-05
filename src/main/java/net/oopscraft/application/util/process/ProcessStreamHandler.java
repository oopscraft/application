package net.oopscraft.application.util.process;

public interface ProcessStreamHandler {
	
	/**
	 * Reads standard output stream
	 * @param line
	 */
	public void readLine(String line);
	
}
