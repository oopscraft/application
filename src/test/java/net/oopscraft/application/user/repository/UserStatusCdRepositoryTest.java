package net.oopscraft.application.user.repository;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import net.oopscraft.application.core.TextTable;
import net.oopscraft.application.test.ApplicationTestRunner;
import net.oopscraft.application.user.UserStatusCd;

public class UserStatusCdRepositoryTest extends ApplicationTestRunner {
	
	UserStatusCdRepository userStatusCdRepository;

	public UserStatusCdRepositoryTest() throws Exception {
		super();
	}
	
	@Before
	public void before() throws Exception {
		userStatusCdRepository = this.getJpaRepository(UserStatusCdRepository.class);
	}

	@Test
	public void test() throws Exception {
		List<UserStatusCd> userStatusCds = userStatusCdRepository.findAllByOrderByDisplaySeqAsc();
		System.out.println(new TextTable(userStatusCds));
	}
}
