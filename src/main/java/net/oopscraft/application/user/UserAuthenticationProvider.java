/*
 * Copyright since 2002 oopscraft.net
 *
 * Everyone is permitted to copy and distribute verbatim copies of this license document, 
 * but changing it is not allowed.
 * Released under the LGPL-3.0 licence
 * https://opensource.org/licenses/lgpl-3.0.html
 */
package net.lotte.chamomile.commons.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * @author chomookun
 *
 */
public class UserAuthenticationProvider implements AuthenticationProvider {
	
	@Autowired
	UserService userService;

	/* (non-Javadoc)
	 * @see org.springframework.security.authentication.AuthenticationProvider#authenticate(org.springframework.security.core.Authentication)
	 */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
		// getting user id and password
		String id = authentication.getName();
		String password = (String) authentication.getCredentials();
		
		// loading user information
		User user = userService.loadUserByUsername(id);
		if(user.getPassword().equals(password) == false) {
			throw new BadCredentialsException("password is invalid");
		}
		
		// Retrieves user authority
		try {
			List<Authority> authorityList = userService.getUserAuthorityList(id);
			user.setAuthorityList(authorityList);
		}catch(Exception e) {
			throw new AuthenticationServiceException(e.getMessage(),e);
		}

		// return authentication token.
		authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorityList());
		return authentication;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.authentication.AuthenticationProvider#supports(java.lang.Class)
	 */
	@Override
	public boolean supports(Class<?> authentication) {
		return true;
	}

}
