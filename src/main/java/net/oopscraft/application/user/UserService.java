/*
 * Copyright since 2002 oopscraft.net
 *
 * Everyone is permitted to copy and distribute verbatim copies of this license document, 
 * but changing it is not allowed.
 * Released under the LGPL-3.0 licence
 * https://opensource.org/licenses/lgpl-3.0.html
 */
package net.oopscraft.application.user;

import java.util.List;

import org.hibernate.SessionFactory;
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
	
	public enum FindBy { ID_LIKE, NAME_LIKE }
	
	@Autowired
	UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			User user = userRepository.findOne(username);
			if(user == null) {
				throw new UsernameNotFoundException(String.format("user[%s] is not found.",username));
			}
			return (UserDetails)user;
		}catch(Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new UsernameNotFoundException(e.getMessage(), e);
		}
	}
	
	/**
	 * Gets list of user by search condition and value
	 * @param findBy
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public List<User> getUsers(FindBy findBy, String value) throws Exception {
		List<User> users = null;
		if(findBy == null) {
			users = userRepository.findAll();
		}else {
			switch(findBy) {
			case ID_LIKE:
				users = userRepository.findByIdLike(value);
			break;
			case NAME_LIKE:
				users = userRepository.findByNameLike(value);
			break;
			}
		}
		return users;
	}
	
	public User getUser(String id) throws Exception {
		User user = userRepository.findOne(id);
		user.getGroups();
		user.getRoles();
		user.getAuthorities();
		return user;
	}
	
	/**
	 * Saves user details
	 * @param user
	 * @throws Exception
	 */
	public void saveUser(User user) throws Exception {
		userRepository.save(user);
	}
	
	/**
	 * Removes group details
	 * @param id
	 * @throws Exception
	 */
	public void removeUser(String id) throws Exception {
		userRepository.delete(id);
	}
	

}
