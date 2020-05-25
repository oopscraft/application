package net.oopscraft.application.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import net.oopscraft.application.core.JsonConverter;
import net.oopscraft.application.property.PropertyService;
import net.oopscraft.application.user.User;
import net.oopscraft.application.user.UserService;


public class AuthenticationProvider implements org.springframework.security.authentication.AuthenticationProvider {
	
	@Autowired
	UserService userService;
	
	@Autowired
	PropertyService propertyService;
	
	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	/* (non-Javadoc)
	 * @see org.springframework.security.authentication.AuthenticationProvider#authenticate(org.springframework.security.core.Authentication)
	 */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
		// name is email
		String username = authentication.getName();
		String password = (String) authentication.getCredentials();
		
		// retrieves user data
		User user = null;
		try {
			user = userService.getUserByEmail(username);
			if(user == null) {
				throw new UsernameNotFoundException("User not found");
			}
		}catch(UsernameNotFoundException e) {
			throw e;
		}catch(Exception e) {
			throw new UsernameNotFoundException(e.getMessage());
		}
		
		
		// checking password
		try {
			if(userService.isCorrectPassword(user.getId(), password) == false) {
				throw new BadCredentialsException("Password is incorrect.");
			}
		}catch(BadCredentialsException e) {
			throw e;
		}catch(Exception e) {
			throw new BadCredentialsException(e.getMessage());
		}

		// return authentication token.
		UserDetails userDetails = new UserDetails(user);
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
	
	/**
	 * encode
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public String encodeAccessToken(UserDetails userDetails) throws Exception {
		String jwt = Jwts.builder()
				  .setExpiration(new Date(System.currentTimeMillis() + (getAccessTokenExpirationMinutes()*60*1000)))
				  .claim("userDetails", JsonConverter.toJson(userDetails))
				  .signWith(SignatureAlgorithm.HS256, getAccessTokenSecretKey())
				  .compact();
		return jwt;
	}
	
	/**
	 * decode
	 * @param accessToken
	 * @return
	 * @throws Exception
	 */
	public UserDetails decodeAccessToken(String accessToken) throws Exception {
        Claims claims = Jwts.parser()
        		.setSigningKey(getAccessTokenSecretKey())
        		.parseClaimsJws(accessToken).getBody();
        String userDetailsClaim = (String)claims.get("userDetails");
        UserDetails userDetails = JsonConverter.toObject(userDetailsClaim, UserDetails.class);
		return userDetails;
	}

	private byte[] getAccessTokenSecretKey() throws Exception {
		return propertyService.getProperty("APP_TOKN_SCRT_KEY").getValue().getBytes("UTF-8");
	}
	
	private int getAccessTokenExpirationMinutes() throws Exception {
		return Integer.parseInt(propertyService.getProperty("APP_TOKN_EXPR_MINS").getValue().trim());
	}
}
