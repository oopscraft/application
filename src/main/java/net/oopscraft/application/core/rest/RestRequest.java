package net.oopscraft.application.core.rest;

import java.util.LinkedHashMap;
import java.util.Map;

public class RestRequest { 

	String id = null;
	String name = null;
	String uri = null;
	Map<String,String> customHeaders = new LinkedHashMap<String,String>();
	String method = null;
	int timeout = 30;	// default idle timeout is 30 seconds. 
	String contentType = null;
	String payload = null;
	String test = null;
	
	public String getId() { 
		return id;
	} 
	
	public void setId(String id) { 
		this.id = id;
	} 
	
	public String getName() { 
		return name;
	} 
	
	public void setName(String name) { 
		this.name = name;
	} 
	
	public String getUri() { 
		return uri;
	} 
	
	public void setUri(String uri) { 
		this.uri = uri;
	} 
	
	public String getMethod() { 
		return method;
	} 
	
	public void setMethod(String method) { 
		this.method = method;
	} 
	
	public void addCustomHeader(String key, String value) { 
		customHeaders.put(key, value);
	} 
	
	public Object getCustomHeader(String key){ 
		return customHeaders.get(key);
	} 
	
	public Map<String,String> getCustomHeaders() { 
		return customHeaders;
	} 
	
	public int getTimeout() { 
		return timeout;
	} 
	
	public void setTimeout(int timeout) { 
		this.timeout = timeout;
	} 
	
	public String getContentType() { 
		return contentType;
	} 
	
	public void setContentType(String contentType) { 
		this.contentType = contentType;
	} 
	
	public String getPayload() { 
		return payload;
	} 
	
	public void setPayload(String payload) { 
		this.payload = payload;
	} 
	
	public String getTest() { 
		return test;
	} 
	
	public void setTest(String test) { 
		this.test = test;
	}

}