package net.oopscraft.application.core.rest;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import net.oopscraft.application.core.TextTableBuilder;

public class RestRequestFactoryTest {

	@Test
	public void test() {
		try {
			RestRequestFactory restRequestFactory = RestRequestFactory.getInstance("/net/oopscraft/application/core/rest/RestRequest.xml");
			List<RestRequest> restRequestList = restRequestFactory.getRestRequestList();
			System.out.println(TextTableBuilder.build(restRequestList));
			assert(true);
		}catch(Exception e) {
			e.printStackTrace(System.err);
			assert(false);
		}
		
	}

}
