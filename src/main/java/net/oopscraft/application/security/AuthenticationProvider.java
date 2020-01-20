package net.oopscraft.application.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import net.oopscraft.application.common.TextTable;
import net.oopscraft.application.user.UserService;

@Component
public class AuthenticationProvider implements org.springframework.security.authentication.AuthenticationProvider {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationProvider.class);
	
	@Autowired
	UserService userService;
	
	@Autowired
	UserDetailsService userDetailsService;
	
	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	/* (non-Javadoc)
	 * @see org.springframework.security.authentication.AuthenticationProvider#authenticate(org.springframework.security.core.Authentication)
	 */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
		// getting user id and password
		String id = authentication.getName();
		String password = (String) authentication.getCredentials();
		
		// loading user information
		UserDetails userDetails = userDetailsService.loadUserByUsername(id);
		
		// checking password
		if(userService.isValidPassword(id, password) == false) {
			throw new BadCredentialsException("password is incorrect.");
		}

		// return authentication token.
		LOGGER.debug("{}", new TextTable(userDetails.getAuthorities()));
		authentication = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
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
