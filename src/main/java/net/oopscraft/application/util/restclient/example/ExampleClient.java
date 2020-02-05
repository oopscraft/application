package net.oopscraft.application.util.restclient.example;

import net.oopscraft.application.util.restclient.RestParam;

public interface ExampleClient {
	
	public String getTest(@RestParam("bid")String bid, @RestParam("name")String name) throws Exception;
	
}
