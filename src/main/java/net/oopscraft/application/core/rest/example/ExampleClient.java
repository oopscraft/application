package net.oopscraft.application.core.rest.example;

import net.oopscraft.application.core.rest.RestParam;

public interface ExampleClient {
	
	public String getTest(@RestParam("bid")String bid, @RestParam("name")String name) throws Exception;
	
}
