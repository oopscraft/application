package net.oopscraft.application.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import net.oopscraft.application.core.JsonConverter;
import net.oopscraft.application.user.UserService;
import net.oopscraft.application.user.entity.Authority;
import net.oopscraft.application.user.entity.User;


public class AuthenticationProvider implements org.springframework.security.authentication.AuthenticationProvider {
	
	private String secretKey = "temporary";
	private int sessionTimeout = 10;

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
		if(userService.isCorrectPassword(id, password) == false) {
			throw new BadCredentialsException("password is incorrect.");
		}

		// return authentication token.
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
	public String encodeAccessToken(User user) throws Exception {
		String jwt = Jwts.builder()
				  .setExpiration(new Date(System.currentTimeMillis() + (sessionTimeout*60*1000)))
				  .claim("id", user.getId())
				  .claim("name", user.getName())
				  .claim("nickName", user.getNickname())
				  .claim("status", user.getStatus())
				  .claim("authorities", JsonConverter.toJson(user.getAuthorities()))
				  .signWith(SignatureAlgorithm.HS256, secretKey.getBytes("UTF-8"))
				  .compact();
		return jwt;
	}
	
	/**
	 * decode
	 * @param accessToken
	 * @return
	 * @throws Exception
	 */
	public User decodeAccessToken(String accessToken) throws Exception {
        Claims claims = Jwts.parser()
        		.setSigningKey(secretKey.getBytes("UTF-8"))
        		.parseClaimsJws(accessToken).getBody();
    	String id = (String)claims.get("id");
    	String name = (claims.get("name") == null ? null : (String)claims.get("name"));
    	String nickname = (claims.get("nickname") == null ? null : (String)claims.get("nickname"));
    	String status = (claims.get("status") == null ? null : (String)claims.get("status"));
    	String authorities = (claims.get("authorities") == null ? null : (String)claims.get("authorities"));
    	User user = new User();
    	user.setId(id);
    	user.setName(name);
    	user.setNickname(nickname);
    	user.setStatus(User.Status.valueOf(status));
    	user.setAuthorities(JsonConverter.toList(authorities, Authority.class));
		return user;
	}


}
