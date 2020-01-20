package net.oopscraft.application.common.restclient;

import java.util.LinkedHashMap;
import java.util.Map;

public class RestResponse { 

	Map<String,String> headerMap = new LinkedHashMap<String,String>();
	int responseCode = -1;
	String responseMessage;
	
	public Map<String, String> getHeaderMap() {
		return headerMap;
	}
	public void setHeaderMap(Map<String, String> headerMap) {
		this.headerMap = headerMap;
	}
	public int getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}
	public String getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

}