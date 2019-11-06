package net.oopscraft.application.admin.controller;

import org.junit.Test;
import org.springframework.security.test.context.support.WithMockUser;

import net.oopscraft.application.core.JsonUtility;
import net.oopscraft.application.test.ApplicationTestRunnerWithSpring;
import net.oopscraft.application.user.entity.Group;

@WithMockUser(username = "junit", authorities = {"ADMIN","ADMIN_GROUP"})
public class GroupControllerTest extends ApplicationTestRunnerWithSpring {
	
	@Test
	public void testGet() throws Exception {
		this.performGet("/admin/group");
		assert(true);
	}

	@Test
	public void testGetGroups() throws Exception {
		this.performGet("/admin/group/getGroups");
		assert(true);
	}
	
	@Test
	public void testGetGroup() throws Exception {
		this.performGet("/admin/group/getGroup?id=junit");
		assert(true);
	}
	
	@Test
	public void testSaveGroup() throws Exception {
		Group group = new Group();
		group.setId("junit");
		group.setName("junit");
		String payload = JsonUtility.toJson(group);
		this.performPostJson("/admin/user/saveGroup", payload);
		assert(true);
	}
	
	@Test
	public void testDeleteGroup() throws Exception {
		this.performGet("/admin/user/deleteGroup?id=junit");
		assert(true);
	}

}
