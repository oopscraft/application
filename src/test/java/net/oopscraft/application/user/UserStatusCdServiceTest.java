package net.oopscraft.application.user;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import net.oopscraft.application.core.TextTable;
import net.oopscraft.application.test.ApplicationSpringTestRunner;

public class UserStatusCdServiceTest extends ApplicationSpringTestRunner {

	@Autowired
	UserStatusCdService userStatusCdService;
	
	@Test
	public void test() throws Exception {
		List<UserStatusCd> userStatusCds = userStatusCdService.getUserStatusCds();
		System.out.println(new TextTable(userStatusCds));
		assert(true);
	}
}
