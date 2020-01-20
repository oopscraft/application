package net.oopscraft.application.common.restclient;

public interface RestListener {
	
	/**
	 * before
	 * @param restRequest
	 * @throws RestException
	 */
	public void before(RestRequest restRequest) throws RestException;
	
	/**
	 * after
	 * @throws RestException
	 */
	public void after(RestResponse restResponse) throws RestException;

	
}
