package net.oopscraft.application.core.rest;

import org.junit.Test;

import net.oopscraft.application.util.restclient.RestClientFactory;
import net.oopscraft.application.util.restclient.example.ExampleClient;

public class RestClientFactoryTest {

	@Test
	public void test() {
		try {
			String host = "http://marketdata.krx.co.kr";
			String resourceXml = "/net/oopscraft/application/core/rest/RestRequest.xml";
			String bid = "COM";
			String name = "selectbox";
			ExampleClient restClient = RestClientFactory.getRestClient(ExampleClient.class, resourceXml, host);
			String result = restClient.getTest(bid, name);
			System.out.println(result);
			assert(true);
		}catch(Exception e) {
			e.printStackTrace(System.err);
			assert(false);
		}
		
	}

}
