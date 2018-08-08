/*
 * Copyright since 2002 oopscraft.net
 *
 * Everyone is permitted to copy and distribute verbatim copies of this license document, 
 * but changing it is not allowed.
 * Released under the LGPL-3.0 licence
 * https://opensource.org/licenses/lgpl-3.0.html
 */
package net.oopscraft.application.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

	@Autowired
	UserDao userDao;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			User user = userDao.selectUser(username);
			if(user == null) {
				throw new UsernameNotFoundException(String.format("user[%s] is not found.",username));
			}
			return (UserDetails) user;
		}catch(Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new UsernameNotFoundException(e.getMessage(), e);
		}
	}
	

}
