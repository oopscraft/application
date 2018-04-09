/*
 * Copyright since 2002 oopscraft.net
 *
 * Everyone is permitted to copy and distribute verbatim copies of this license document, 
 * but changing it is not allowed.
 * Released under the LGPL-3.0 licence
 * https://opensource.org/licenses/lgpl-3.0.html
 */
package net.oopscraft.application.core.security;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import net.oopscraft.application.ApplicationTest;
import net.oopscraft.application.core.TextTableBuilder;
import net.oopscraft.application.user.User;
import net.oopscraft.application.user.dao.UserDao;

/**
 * @author chomookun@gmail.com
 *
 */
public class UserDaoTest extends ApplicationTest {
	
	@Autowired
	UserDao userDao;
	
	@Test
	public void handleUser() throws Exception {
		try {
			// insert new user
			User user = new User();
			user.setId("test");
			user.setEmail("test@email.com");
			user.setMobile("01012341234");
			user.setName("Tester");
			user.setNickname("Tester");
			user.setPassword("password");
			user.setStatusCode("A");
			userDao.insertUser(user);
			user = userDao.selectUser("test");
			System.out.println(TextTableBuilder.build(user));
			if(user == null) {
				System.err.println("insertUser Error");
				assert(false);
			}
			
			// update user
			user.setName("test2");
			userDao.updateUser(user);
			user = userDao.selectUser("test");
			System.out.println(TextTableBuilder.build(user));
			if(!"test2".equals(user.getName())) {
				System.err.println("updateUser Error");
				assert(false);
			}
			
			// delete user
			userDao.deleteUser(user.getId());
			user = userDao.selectUser("test");
			System.out.println(TextTableBuilder.build(user));
			if(user != null) {
				System.err.println("deleteUser Error");
				assert(false);
			}
			
		}catch(Exception e) {
			e.printStackTrace(System.err);
			assert(false);
		}
		assert(true);
	}

//	@Test
//	public void handleGroup() throws Exception {
//		try {
//			// insert group list
//			for(int i = 0; i < 3; i ++) {
//				String groupId = String.valueOf(i); 
//				Group group = new Group();
//				group.setId(groupId);
//				group.setSortSeq(i);
//				group.setName(String.format("Name[%s]", groupId));
//				group.setDescription("this is description");
//				userDao.insertGroup(group);
//				for(int ii = 0; ii < 3; ii ++ ) {
//					String childGroupId = groupId + "-" + String.valueOf(ii);
//					Group childGroup = new Group();
//					childGroup.setId(childGroupId);
//					childGroup.setUpperId(groupId);
//					childGroup.setSortSeq(ii);
//					childGroup.setName(String.format("Name[%s]", childGroupId));
//					childGroup.setDescription("this is description");
//					userDao.insertGroup(childGroup);
//				}
//			}
//
//			// test selectGroupList
//			List<Group> groupList = userDao.selectGroupList();
//			System.out.println(TextTableBuilder.build(groupList));
//			
//			// test selectChildGroupList
//			List<Group> childGroupList = userDao.selectChildGroupList("1");
//			System.out.println(TextTableBuilder.build(childGroupList));
//			
//		}catch(Exception e) {
//			e.printStackTrace(System.err);
//			assert(false);
//		}
//		assert(true);
//	}
	
}
