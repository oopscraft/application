/*
 * Copyright since 2002 oopscraft.net
 *
 * Everyone is permitted to copy and distribute verbatim copies of this license document, 
 * but changing it is not allowed.
 * Released under the LGPL-3.0 licence
 * https://opensource.org/licenses/lgpl-3.0.html
 */
package net.oopscraft.application.core.user;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import net.oopscraft.application.ApplicationTest;
import net.oopscraft.application.core.TextTableBuilder;

/**
 * @author chomookun@gmail.com
 *
 */
public class UserDaoTest extends ApplicationTest {
	
	@Autowired
	UserDao userDao;
	
	@Test
	public void insertUser() throws Exception {
		try {
			// insert new user
			User user = new User();
			user.setId("test");
			user.setEmail("test@email.com");
			user.setMobileNumber("01012341234");
			user.setName("Tester");
			user.setNickname("Tester");
			user.setPassword("password");
			userDao.insertUser(user);

			// select new user
			user = userDao.selectUser("test");
			System.out.println(TextTableBuilder.build(user));
		}catch(Exception e) {
			e.printStackTrace(System.err);
			assert(false);
		}
		assert(true);
	}

}
