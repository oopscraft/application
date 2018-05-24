package net.oopscraft.application.core.restclient;

public class RestClientProxyFactory {
	
	public static RestClientProxy getRestClientProxy() throws Exception {
		return new RestClientProxy();
	}

}
