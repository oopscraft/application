package net.oopscraft.application.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import net.oopscraft.application.user.UserService;
import net.oopscraft.application.user.entity.User;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService{
	
	@Autowired
	UserService userService;

	@Override
	public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
		// loading user information
		User user = new User(id);
		try {
			user = userService.getUser(user.getId());
		} catch (Exception e) {
			throw new UsernameNotFoundException(e.getMessage(), e);
		}
		if(user == null) {
			throw new UsernameNotFoundException(String.format("user[%s] not found.", id));	
		}
		
		UserDetails userDetails = new UserDetails(user);
		return userDetails;
	}

}
