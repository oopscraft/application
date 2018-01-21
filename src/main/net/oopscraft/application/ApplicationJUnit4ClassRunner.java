/*
 * Copyright since 2002 oopscraft.net
 *
 * Everyone is permitted to copy and distribute verbatim copies of this license document, 
 * but changing it is not allowed.
 * Released under the LGPL-3.0 licence
 * https://opensource.org/licenses/lgpl-3.0.html
 */
package net.oopscraft.application;

import org.junit.runners.model.InitializationError;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author chomookun@gmail.com
 *
 */
public class ApplicationJUnit4ClassRunner extends SpringJUnit4ClassRunner {
	
	/**
	 * @param clazz
	 * @throws InitializationError
	 */
	public ApplicationJUnit4ClassRunner(Class<?> clazz) throws InitializationError {
		super(clazz);
		try {
			ApplicationContainer.launchForTest(Application.class, new String[] {});
		}catch(Exception e){
			e.printStackTrace(System.err);
			throw new InitializationError(e);
		}
	}

}
