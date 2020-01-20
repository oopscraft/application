package net.oopscraft.application.core;

import org.junit.Test;

import net.oopscraft.application.common.EncodeUtility;

public class RandomUtilsTest {
	
	@Test
	public void testGenerateUUID() throws Exception {
		String id = EncodeUtility.generateUUID();
		System.out.println(id);
	}

}
