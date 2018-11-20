package net.oopscraft.application.core;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JsonUtils {
	
	public static String toJson(Object obj) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
	}
	
	public static String toJson(List<?> list) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
		return mapper.writeValueAsString(list);
	}
	
	public static <T> T toObject(String json, Class<T> clazz) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return mapper.readValue(json, clazz);
	}
	
	public static <T> List<T> toList(String json, Class<T> clazz) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return mapper.readValue(json, TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, clazz));
	}
	
	public static String stringify(String string) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(string);
		return mapper.writeValueAsString(json);
	}
	
	public static boolean isJson(String value) {
	    boolean valid = true;
	    try{
	    	ObjectMapper mapper = new ObjectMapper();
	    	mapper.readTree(value);
	    } catch(Exception e){
	        valid = false;
	    }
	    return valid;
	}

}
