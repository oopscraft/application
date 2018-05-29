package net.oopscraft.application.core.rest;

public interface RestClient {
	
	public String getTest(@RestParam("bid")String bid, @RestParam("name")String name) throws Exception;
	
}
