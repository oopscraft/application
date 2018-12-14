package net.oopscraft.application.user;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import net.oopscraft.application.core.TextTable;
import net.oopscraft.application.test.ApplicationSpringTestRunner;

public class UserStatusServiceTest extends ApplicationSpringTestRunner {

	@Autowired
	UserStatusService userStatusService;
	
	@Test
	public void test() throws Exception {
		List<UserStatus> userStatusCds = userStatusService.getUserStatuses();
		System.out.println(new TextTable(userStatusCds));
		assert(true);
	}
}
