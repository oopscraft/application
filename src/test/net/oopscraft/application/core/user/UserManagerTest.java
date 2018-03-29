/*
 * Copyright since 2002 oopscraft.net
 *
 * Everyone is permitted to copy and distribute verbatim copies of this license document, 
 * but changing it is not allowed.
 * Released under the LGPL-3.0 licence
 * https://opensource.org/licenses/lgpl-3.0.html
 */
package net.oopscraft.application.core.user;

import java.util.List;

import org.junit.Test;

import net.oopscraft.application.ApplicationTest;
import net.oopscraft.application.core.TextTableBuilder;
import net.oopscraft.application.core.security.Group;
import net.oopscraft.application.core.security.UserFactory;

/**
 * @author chomookun@gmail.com
 *
 */
public class UserManagerTest extends ApplicationTest {
	
	@Test
	public void getGroupList() {
		try {
			UserFactory userManager = new UserFactory();
			List<Group> groupList = userManager.getGroupList();
			System.out.println(TextTableBuilder.build(groupList));
		}catch(Exception e) {
			e.printStackTrace(System.err);
			assert(false);
		}
		assert(true);
	}

}
