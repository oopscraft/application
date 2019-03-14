package net.oopscraft.application.core;

import org.junit.Test;

public class RandomUtilsTest {
	
	@Test
	public void testGenerateUUID() throws Exception {
		String id = RandomUtils.generate();
		System.out.println(id);
	}

}
