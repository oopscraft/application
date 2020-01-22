package net.oopscraft.application.util.restclient;

import java.lang.reflect.Proxy;

public class RestClientFactory {
	
	@SuppressWarnings("unchecked")
	public static <T>T getRestClient(Class<T> clazz, String restRequestXml, String host) throws Exception { 
		RestClientHandler restClientHandler = new RestClientHandler();
		RestRequestFactory restRequestFactory = RestRequestFactory.getInstance(restRequestXml);
		restClientHandler.setRestRequestFactory(restRequestFactory);
		restClientHandler.setHost(host);
		
		// returns proxy instance
		return (T) Proxy.newProxyInstance(
				clazz.getClassLoader(),
                new Class[] { clazz },
                restClientHandler);
	}

}
