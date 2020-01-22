package net.oopscraft.application.util.process;

public class ProcessException extends Exception {

	private static final long serialVersionUID = -4190862617683348481L;
	
	private int exitValue;
	
	public ProcessException(int exitValue, String message) {
		super(message);
		this.exitValue = exitValue;
	}

	public int getExitValue() {
		return exitValue;
	}

}
