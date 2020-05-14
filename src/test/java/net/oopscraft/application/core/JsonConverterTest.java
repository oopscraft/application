package net.oopscraft.application.core;

import org.junit.Test;

public class JsonConverterTest {
	
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
	
	@Test
	public void testJsonPropertyAnnotation() throws Exception {
		Pagination pagination = new Pagination(99,999);
		String jsonString = JsonConverter.toJson(pagination);
		System.out.println(jsonString);
		
		
		pagination = JsonConverter.toObject(jsonString, Pagination.class);
		System.out.println(pagination.getPage() + "/" + pagination.getRows());
		
		
		
	}

}
