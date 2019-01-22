package net.oopscraft.application.user.security;

import org.junit.Test;

import net.oopscraft.application.core.TextTable;
import net.oopscraft.application.user.User;

public class AccessTokenEncoderTest {
	
	@Test
	public void testEncode() throws Exception {
		AccessTokenEncoder accessTokenEncoder = new AccessTokenEncoder();
		User user = new User();
		user.setId("Junit");
		String token = accessTokenEncoder.encode(user);
		System.out.println(String.format("token[%s]", token));
		assert(true);
	}
	
	@Test
	public void testDecode() throws Exception {
		AccessTokenEncoder accessTokenEncoder = new AccessTokenEncoder();
		User user = new User();
		user.setId("Junit");
		System.out.println(new TextTable(user));
		String token = accessTokenEncoder.encode(user);
		System.out.println(String.format("token[%s]", token));
		user = accessTokenEncoder.decode(token);
		System.out.println(new TextTable(user));
		assert(true);
	}

}
