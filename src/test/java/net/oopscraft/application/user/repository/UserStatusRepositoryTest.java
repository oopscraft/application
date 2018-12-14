package net.oopscraft.application.user.repository;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import net.oopscraft.application.core.TextTable;
import net.oopscraft.application.test.ApplicationTestRunner;
import net.oopscraft.application.user.UserStatus;

public class UserStatusRepositoryTest extends ApplicationTestRunner {
	
	UserStatusRepository userStatusCdRepository;

	public UserStatusRepositoryTest() throws Exception {
		super();
	}
	
	@Before
	public void before() throws Exception {
		userStatusCdRepository = this.getJpaRepository(UserStatusRepository.class);
	}

	@Test
	public void test() throws Exception {
		List<UserStatus> userStatusCds = userStatusCdRepository.findAllByOrderByDisplaySeqAsc();
		System.out.println(new TextTable(userStatusCds));
	}
}
