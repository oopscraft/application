package net.oopscraft.application.core;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JsonUtility {
	
	public static ObjectMapper objectMapper = new ObjectMapper();
	static {
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
		objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	
	public static ObjectMapper getObjectMapper() {
		return objectMapper;
	}
	
	public static String toJson(Object obj) throws Exception {
		return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
	}
	
	public static String toJson(List<?> list) throws Exception {
		return objectMapper.writeValueAsString(list);
	}
	
	public static String toJson1(Enum<?> enm) throws Exception {
		return null;
	}
	
	public static <T> T toObject(String json, Class<T> clazz) throws Exception {
		return objectMapper.readValue(json, clazz);
	}
	
	public static <T> List<T> toList(String json, Class<T> clazz) throws Exception {
		return objectMapper.readValue(json, TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, clazz));
	}
	
	public static String stringify(String string) throws Exception {
		String json = objectMapper.writeValueAsString(string);
		return objectMapper.writeValueAsString(json);
	}
	
	public static boolean isJson(String value) {
	    boolean valid = true;
	    try{
	    	objectMapper.readTree(value);
	    } catch(Exception e){
	        valid = false;
	    }
	    return valid;
	}

}
