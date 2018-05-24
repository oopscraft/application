package net.oopscraft.application.core.restclient;

import java.lang.reflect.Proxy;

public class RestClientFactory {
	
	@SuppressWarnings("unchecked")
	public <T> T getRestClient(Class<T> clazz) throws Exception {
		RestClientProxy restClientProxy = RestClientProxyFactory.getRestClientProxy();
		return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] { clazz }, restClientProxy);
	}
	

}
