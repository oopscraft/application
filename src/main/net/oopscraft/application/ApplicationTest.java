package net.oopscraft.application;

import org.junit.Before;

public class ApplicationTest {
	
	@Before
	public void before() {
		try {
			ApplicationContainer.launchForTest(Application.class, new String[] {});
		}catch(Exception e){
			e.printStackTrace(System.err);
			assert(false);
		}
	}


}
