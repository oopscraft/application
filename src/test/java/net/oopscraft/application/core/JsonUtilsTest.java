package net.oopscraft.application.core;

import org.junit.Test;

public class JsonUtilsTest {
	
	@Test
	public void testToJsonMap() throws Exception {
		String jsonString = JsonUtils.toJson(new ValueMap());
		System.out.println(jsonString);
	}
	
	@Test
	public void testToJsonObject() throws Exception {
		String jsonString = JsonUtils.toJson(new Object());
		System.out.println(jsonString);
	}
	
	enum Enums { A,B,C }
	
	@Test
	public void testToJsonEnum() throws Exception {
		String jsonString = JsonUtils.toJson(Enums.values());
		System.out.println(jsonString);
	}

}
