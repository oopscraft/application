package net.oopscraft.application.core.restclient.example;

import net.oopscraft.application.core.restclient.RestParam;

public interface ExampleClient {
	
	public String getTest(@RestParam("bid")String bid, @RestParam("name")String name) throws Exception;
	
}
