package net.oopscraft.application;

public class Application {
	
	protected final ApplicationContext context;
	
	/**
	 * constructor
	 * @param configureFile
	 */
	public Application(ApplicationContext context) {
		this.context = context;
	}
	
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
	 * Returns ApplicaitonContext.
	 * @return
	 */
	public final ApplicationContext getContext() {
		return this.context;
	}
	
	/**
	 * main
	 * @param args
	 * @throws Exception
	 */
	public static void main(final String[] args) throws Exception {
		ApplicationContainer.launch(Application.class, args);
	}

}
