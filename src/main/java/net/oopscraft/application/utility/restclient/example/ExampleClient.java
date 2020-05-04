package net.oopscraft.application.utility.restclient.example;

import net.oopscraft.application.utility.restclient.RestParam;

public interface ExampleClient {
	
	public String getTest(@RestParam("bid")String bid, @RestParam("name")String name) throws Exception;
	
}
