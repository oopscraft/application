package net.oopscraft.application.admin.controller;

import org.junit.Test;
import org.springframework.security.test.context.support.WithMockUser;

import net.oopscraft.application.ApplicationTestRunnerWithSpring;
import net.oopscraft.application.core.JsonConverter;
import net.oopscraft.application.core.ValueMap;
import net.oopscraft.application.user.entity.User;

@WithMockUser(username = "junit", authorities = {"ADMIN","ADMIN_USER"})
public class UserControllerTest extends ApplicationTestRunnerWithSpring {
	
	@Test
	public void testGet() throws Exception {
		this.performGet("/admin/user");
		assert(true);
	}

	@Test
	public void testGetUsers() throws Exception {
		this.performGet("/admin/user/getUsers");
		assert(true);
	}
	
	@Test
	public void testGetUser() throws Exception {
		this.performGet("/admin/user/getUser?id=junit");
		assert(true);
	}
	
	@Test
	public void testSaveUser() throws Exception {
		User user = new User();
		user.setId("junit");
		user.setPassword("password");
		user.setName("junit");
		user.setNickname("junit");
		user.setEmail("junit@gmail.com");
		user.setMobile("010-1234-1234");
		ValueMap userMap = JsonConverter.toObject(JsonConverter.toJson(user), ValueMap.class);
		userMap.set("password", "1234");
		String payload = JsonConverter.toJson(userMap);
		this.performPostJson("/admin/user/saveUser", payload);
		assert(true);
	}
	
	@Test
	public void testDeleteUser() throws Exception {
		this.performGet("/admin/user/deleteUser?id=junit");
		assert(true);
	}

}
