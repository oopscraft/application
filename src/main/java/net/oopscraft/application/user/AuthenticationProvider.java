/*
 * Copyright since 2002 oopscraft.net
 *
 * Everyone is permitted to copy and distribute verbatim copies of this license document, 
 * but changing it is not allowed.
 * Released under the LGPL-3.0 licence
 * https://opensource.org/licenses/lgpl-3.0.html
 */
package net.oopscraft.application.user;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author chomookun
 *
 */
public class AuthenticationProvider implements org.springframework.security.authentication.AuthenticationProvider {
	
	@Autowired
	UserService userDetailsService;
	
	@Autowired
	PasswordEncoder passwordEncoder;

	/* (non-Javadoc)
	 * @see org.springframework.security.authentication.AuthenticationProvider#authenticate(org.springframework.security.core.Authentication)
	 */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
		
		// getting user id and password
		String id = authentication.getName();
		String password = (String) authentication.getCredentials();
		
		// loading user information
		org.springframework.security.core.userdetails.UserDetails userDetails = null;
		try {
			userDetails = userDetailsService.loadUserByUsername(id);
		}catch(UsernameNotFoundException e) {
			throw new UsernameNotFoundException(e.getMessage(), e);
		}
		
		// checking password
		if(passwordEncoder.matches(password, userDetails.getPassword()) == false) {
			throw new BadCredentialsException("");
		}

		// return authentication token.
		authentication = new UsernamePasswordAuthenticationToken(id, password, user.getAuthorityList());
		return authentication;
		
		
		
//		// getting user id and password
//		String id = authentication.getName();
//		String password = (String) authentication.getCredentials();
//		
//		// loading user information
//		User user = userService.loadUserByUsername(id);
//		if(user.getPassword().equals(password) == false) {
//			throw new BadCredentialsException("password is invalid");
//		}
//		
//		// Retrieves user authority
//		try {
//			List<Authority> authorityList = userService.getUserAuthorityList(id);
//			user.setAuthorityList(authorityList);
//		}catch(Exception e) {
//			throw new AuthenticationServiceException(e.getMessage(),e);
//		}
//
//		// return authentication token.
//		authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorityList());
//		return authentication;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.authentication.AuthenticationProvider#supports(java.lang.Class)
	 */
	@Override
	public boolean supports(Class<?> authentication) {
		return true;
	}

}
