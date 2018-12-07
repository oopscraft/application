package net.oopscraft.application.test;

import org.junit.runners.model.InitializationError;

import net.oopscraft.application.ApplicationContainer;

public class ApplicationTestRunner {
	
	/**
	 * @param clazz
	 * @throws InitializationError
	 */
	public ApplicationTestRunner() throws Exception {
		try {
			ApplicationContainer.getApplication();
		}catch(Exception e) {
			// prints application error.
			e.printStackTrace(System.err);
		}
	}

}

