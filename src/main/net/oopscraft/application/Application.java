package net.oopscraft.application;

public class Application {
	
	/**
	 * start application
	 * @throws Exception
	 */
	public void start() throws Exception {
		// void
	};
	
	/**
	 * stop application
	 * @throws Exception
	 */
	public void stop() throws Exception {
		// void
	};
	
	/**
	 * main
	 * @param args
	 * @throws Exception
	 */
	public static void main(final String[] args) throws Exception {
		ApplicationContainer.launch(Application.class);
	}

}
