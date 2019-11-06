package net.oopscraft.application.user.repository;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import net.oopscraft.application.ApplicationTestRunner;
import net.oopscraft.application.core.TextTable;
import net.oopscraft.application.user.entity.User;

public class UserRepositoryTest extends ApplicationTestRunner {
	
	private static final String USER_ID = "JUnit";

	UserRepository userRepository;
	
	public UserRepositoryTest() throws Exception {
		super();
	}
	
	@Before
	public void before() throws Exception {
		userRepository = this.getJpaRepository(UserRepository.class);
	}
	
	@Test 
	public void testInsert() throws Exception {
		User user = new User();
		user.setId(USER_ID);
		user.setName(user.getId());
		user.setSignature("User Signature");
		userRepository.save(user);
		userRepository.flush();
		assert(true);
	}
	
	@Test
	public void testUpdate() throws Exception {
		testInsert();	
		User one = userRepository.findOne(USER_ID);
		one.setName("Test Name");
		one.setSignature(null);
		userRepository.save(one);
		userRepository.flush();
		assert(true);
	}
	
	@Test
	public void testFindOne() throws Exception {
		testInsert();
		User one = userRepository.findOne(USER_ID);
		System.out.println(one);
		assert(true);
	}

	@Test
	public void testFindAllByOrderByJoinDateDesc() throws Exception {
		Pageable pageable = new PageRequest(0, 10);
		Page<User> page = userRepository.findAllByOrderBySystemDataYnDescJoinDateDesc(pageable);
		List<User> users = page.getContent();
		System.out.println(new TextTable(users));
		assert(true);
	}

}
