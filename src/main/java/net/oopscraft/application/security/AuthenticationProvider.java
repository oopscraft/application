package net.oopscraft.application.security;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import net.oopscraft.application.core.PasswordBasedEncryptor;
import net.oopscraft.application.property.PropertyService;
import net.oopscraft.application.user.User;
import net.oopscraft.application.user.UserService;


public class AuthenticationProvider implements org.springframework.security.authentication.AuthenticationProvider {
	
	@Autowired
	UserService userService;
	
	@Autowired
	PropertyService propertyService;
	
	@Autowired
	HttpServletRequest request;
	
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
				throw new UsernameNotFoundException("application.global.login.userNotFound");
			}
		}catch(UsernameNotFoundException e) {
			throw e;
		}catch(Exception e) {
			throw new UsernameNotFoundException(e.getMessage());
		}
		
		// checking password
		try {
			if(userService.isCorrectPassword(user.getId(), password) == false) {
				throw new BadCredentialsException("application.global.login.passwordIncorrect");
			}
		}catch(BadCredentialsException e) {
			throw e;
		}catch(Exception e) {
			throw new BadCredentialsException(e.getMessage());
		}
		
		// checking status
		if(user.getStatus() != User.Status.ACTIVE) {
			throw new DisabledException("LOGIN_INVALID_STATUS");
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
	 * encodeToken
	 * @param userDetails
	 * @param timeout
	 * @return
	 * @throws Exception
	 */
	private String encodeToken(UserDetails userDetails, int timeout) throws Exception {
		byte[] tokenSecretKey = getTokenSecretKey();
		PasswordBasedEncryptor passwordBasedEncryptor = new PasswordBasedEncryptor(new String(tokenSecretKey,"UTF-8"));
		String jwt = Jwts.builder()
				  .setExpiration(new Date(System.currentTimeMillis() + (timeout*60*1000)))
				  .claim("id", passwordBasedEncryptor.encrypt(userDetails.getUsername()))
				  .signWith(SignatureAlgorithm.HS256, tokenSecretKey)
				  .compressWith(CompressionCodecs.GZIP)
				  .compact();
		return jwt;
	}
	
	/**
	 * decodeToken
	 * @param token
	 * @return
	 * @throws Exception
	 */
	public UserDetails decodeToken(String token) throws Exception {
		byte[] tokenSecretKey = getTokenSecretKey();
		PasswordBasedEncryptor passwordBasedEncryptor = new PasswordBasedEncryptor(new String(tokenSecretKey,"UTF-8"));
        Claims claims = Jwts.parser()
        		.setSigningKey(tokenSecretKey)
        		.parseClaimsJws(token).getBody();
        String id = passwordBasedEncryptor.decrypt((String)claims.get("id"));
        User user = userService.getUser(id);
        UserDetails userDetails = new UserDetails(user);
		return userDetails;
	}
	
	/**
	 * encode
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public String encodeAccessToken(UserDetails userDetails) throws Exception {
		int accessTokenTimeout = getAccessTokenTimeout();
		return encodeToken(userDetails, accessTokenTimeout);
	}
	
	/**
	 * Encodes refresh token
	 * @param userDetails
	 * @return
	 * @throws Exception
	 */
	public String encodeRefreshToken(UserDetails userDetails) throws Exception {
		int refreshTokenTimeout = getRefreshTokenTimeout();
		return encodeToken(userDetails, refreshTokenTimeout);
	}
	
	public byte[] getTokenSecretKey() throws Exception {
		return propertyService.getProperty("APP_TOKN_SCRT_KEY").getValue().getBytes("UTF-8");
	}

	public int getAccessTokenTimeout() throws Exception {
		return Integer.parseInt(propertyService.getProperty("APP_TOKN_ACES_TMOT").getValue().trim());
	}
	
	public int getRefreshTokenTimeout() throws Exception {
		return Integer.parseInt(propertyService.getProperty("APP_TOKN_REFR_TMOT").getValue().trim());
	}

}
