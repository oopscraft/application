package net.oopscraft.application.user;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import net.oopscraft.application.core.PageInfo;
import net.oopscraft.application.core.TextTable;
import net.oopscraft.application.test.ApplicationSpringTestRunner;

public class UserServiceTest extends ApplicationSpringTestRunner {
	
	private static final String USER_ID = "JUnit";
	
	@Autowired
	UserService userService;
	
	public UserServiceTest() throws Exception {
		super();
	}

	@Test
	public void testGetUsers() throws Exception {
		UserService.SearchCondition searchCondition = userService.new SearchCondition();
		PageInfo pageInfo = new PageInfo(1,20,true);
		List<User> users = userService.getUsers(searchCondition, pageInfo);
		System.out.println(new TextTable(users));
		System.out.println(new TextTable(pageInfo));
	}
	
	@Test
	public void testGetUser() throws Exception {
		User user = userService.getUser(USER_ID);
		System.out.println(new TextTable(user));
	}
	
	@Test
	public void testSaveUser() throws Exception {
		User user = userService.getUser(USER_ID);
		userService.saveUser(user);
	}
	
	@Test
	public void testRemoveUser() throws Exception {
		User user = userService.getUser(USER_ID);
		userService.removeUser(user.getId());
	}
}
