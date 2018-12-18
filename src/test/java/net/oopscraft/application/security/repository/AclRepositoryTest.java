package net.oopscraft.application.security.repository;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import net.oopscraft.application.ApplicationTestRunner;
import net.oopscraft.application.core.TextTable;
import net.oopscraft.application.security.Acl;

public class AclRepositoryTest extends ApplicationTestRunner {
	
	private static String TEST_URI = "/test/*";
	private static String TEST_METHOD = "GET";
	private static String TEST_NAME = "Test ACL";
	
	AclRepository aclRepository;
	
	public AclRepositoryTest() throws Exception {
		super();
	}
	
	@Before
	public void before() throws Exception {
		aclRepository = this.getJpaRepository(AclRepository.class);
	}
	
	@Test
	public void testFindAll() throws Exception {
		List<Acl> acls= aclRepository.findAll();
		System.out.println(new TextTable(acls));
		assert(true);
	}
	
	@Test
	public void testFindOne() throws Exception {
		Acl.AclId aclId = new Acl.AclId(TEST_URI, TEST_METHOD);
		Acl acl = aclRepository.findOne(aclId);
		System.out.println(new TextTable(acl));
		assert(true);
	}
	
	@Test 
	public void testSave() throws Exception {
		Acl acl = new Acl();
		acl.setUri(TEST_URI);
		acl.setMethod(TEST_METHOD);
		acl.setName(TEST_NAME);
		acl = aclRepository.save(acl);
		System.out.println(new TextTable(acl));
		assert(true);
	}
	
	@Test
	public void testDelete() throws Exception{
		Acl acl = new Acl();
		acl.setUri(TEST_URI);
		acl.setMethod(TEST_METHOD);
		aclRepository.save(acl);
		aclRepository.delete(acl);
		aclRepository.flush();
		assert(true);
	}
	
	@Test
	public void testFindAllByOrderBySystemDataYnDescUriAsc() throws Exception {
		Pageable pageable = new PageRequest(0, 10);
		Page<Acl> aclsPage = aclRepository.findAllByOrderBySystemDataYnDescUriAsc(pageable);
		List<Acl> acls = aclsPage.getContent();
		System.out.println(new TextTable(acls));
		assert(true);
	}
	
//	@Test 
//	public void testInsert() throws Exception {
//		User user = new User();
//		user.setId(USER_ID);
//		user.setName(user.getId());
//		user.setSignature("User Signature");
//		aclRepository.save(user);
//		aclRepository.flush();
//		assert(true);
//	}
//	
//	@Test
//	public void testUpdate() throws Exception {
//		testInsert();	
//		User one = aclRepository.findOne(USER_ID);
//		one.setName("Test Name");
//		one.setSignature(null);
//		aclRepository.save(one);
//		aclRepository.flush();
//		assert(true);
//	}
//	
//	@Test
//	public void testFindOne() throws Exception {
//		testInsert();
//		User one = aclRepository.findOne(USER_ID);
//		System.out.println(one);
//		assert(true);
//	}
//
//	@Test
//	public void testFindAllByOrderByJoinDateDesc() throws Exception {
//		Pageable pageable = new PageRequest(0, 10);
//		Page<User> page = aclRepository.findAllByOrderBySystemDataYnDescJoinDateDesc(pageable);
//		List<User> users = page.getContent();
//		System.out.println(new TextTable(users));
//		assert(true);
//	}

}
