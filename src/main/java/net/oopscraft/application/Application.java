package net.oopscraft.application;

import java.io.File;

public class Application {
	
	/**
	 * start application
	 * @throws Exception
	 */
	public void onStart() throws Exception {
		// void
	};
	
	/**
	 * stop application
	 * @throws Exception
	 */
	public void onStop() throws Exception {
		// void
	};
	
	/**
	 * main
	 * @param args
	 * @throws Exception
	 */
	public static void main(final String[] args) throws Exception {
		ApplicationContainer.launch(Application.class, new File("conf/application.xml"), new File("conf/application.properties"));
	}

}
