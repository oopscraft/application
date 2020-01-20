package net.oopscraft.application.common.restclient.example;

import net.oopscraft.application.common.restclient.RestParam;

public interface ExampleClient {
	
	public String getTest(@RestParam("bid")String bid, @RestParam("name")String name) throws Exception;
	
}
