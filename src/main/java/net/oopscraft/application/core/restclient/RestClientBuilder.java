public class RestClientBuilder {
	
	Class<?> clazz;
	String restRequestXml;
	String host;
	List<RestPreProcessor> restPreProcessorList = new ArrayList<RestPreProcessor>();
	
	public RestClientBuilder(Class<?> clazz, String restRequestXml, String host) throws RestException {
		this.clazz = clazz;
		this.restRequestXml = restRequestXml;
		this.host = host;
	}
	
	/**
	 * addRestPreProcessor
	 * @param restPreProcessor
	 */
	public RestClientBuilder addRestPreProcessor(RestPreProcessor restPreProcessor) {
		restPreProcessorList.add(restPreProcessor);
		return this;
	}

	@SuppressWarnings({ "unchecked" })
	public <T>T build() throws RestException { 
		RestClientHandler restClientHandler = new RestClientHandler();
		RestRequestFactory restRequestFactory = RestRequestFactory.getInstance(restRequestXml);
		restClientHandler.setRestRequestFactory(restRequestFactory);
		restClientHandler.setHost(host);
		for(RestPreProcessor restPreProcessor : restPreProcessorList) {
			restClientHandler.addRestPreProcessor(restPreProcessor);
		}
		
		// returns proxy instance
		return (T) Proxy.newProxyInstance(
				clazz.getClassLoader(),
                new Class[] { clazz },
                restClientHandler);
	}
	
}
