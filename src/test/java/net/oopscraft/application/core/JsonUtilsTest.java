package net.oopscraft.application.core;

import org.junit.Test;

import net.oopscraft.application.common.JsonConverter;
import net.oopscraft.application.common.ValueMap;

public class JsonUtilsTest {
	
	@Test
	public void testToJsonMap() throws Exception {
		String jsonString = JsonConverter.toJson(new ValueMap());
		System.out.println(jsonString);
	}
	
	@Test
	public void testToJsonObject() throws Exception {
		String jsonString = JsonConverter.toJson(new Object());
		System.out.println(jsonString);
	}
	
	enum Enums { A,B,C }
	
	@Test
	public void testToJsonEnum() throws Exception {
		String jsonString = JsonConverter.toJson(Enums.values());
		System.out.println(jsonString);
	}

}
