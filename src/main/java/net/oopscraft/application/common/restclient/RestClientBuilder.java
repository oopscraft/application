package net.oopscraft.application.common.restclient;

import java.lang.reflect.Proxy;

public class RestClientBuilder {
	
	Class<?> clazz;
	String restRequestXml;
	String host;
	RestListener restListener;
	
	public RestClientBuilder(Class<?> clazz, String restRequestXml, String host) throws RestException {
		this.clazz = clazz;
		this.restRequestXml = restRequestXml;
		this.host = host;
	}
	
	/**
	 * addRestPreProcessor
	 * @param restPreProcessor
	 */
	public RestClientBuilder setRestListener(RestListener restListener) {
		this.restListener = restListener;
		return this;
	}

	@SuppressWarnings({ "unchecked" })
	public <T>T build() throws RestException { 
		RestClientHandler restClientHandler = new RestClientHandler();
		RestRequestFactory restRequestFactory = RestRequestFactory.getInstance(restRequestXml);
		restClientHandler.setRestRequestFactory(restRequestFactory);
		restClientHandler.setHost(host);
		if(restListener != null) {
			restClientHandler.setRestListener(restListener);
		}
		
		// returns proxy instance
		return (T) Proxy.newProxyInstance(
				clazz.getClassLoader(),
                new Class[] { clazz },
                restClientHandler);
	}
	
}
