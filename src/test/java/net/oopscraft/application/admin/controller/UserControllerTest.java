package net.oopscraft.application.admin.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.security.test.context.support.WithMockUser;

import net.oopscraft.application.test.ApplicationTestRunnerWithSpring;

public class UserControllerTest extends ApplicationTestRunnerWithSpring {

	@Test
	@WithMockUser(username = "junit", authorities= {"ADMIN","ADMIN_USER"})
	public void testGetUsers() throws Exception {
		this.getMockMvc().perform(get("/admin/user/getUsers"))
			.andDo(print())
			.andExpect(status().isOk());
	}

}
