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
import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import net.oopscraft.application.core.JsonConverter;
import net.oopscraft.application.user.UserService;


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
	public String encodeAccessToken(UserDetails userDetails) throws Exception {
		String jwt = Jwts.builder()
				  .setExpiration(new Date(System.currentTimeMillis() + (sessionTimeout*60*1000)))
				  .claim("userDetails", JsonConverter.toJson(userDetails))
				  //.compressWith(CompressionCodecs.GZIP)
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
	public UserDetails decodeAccessToken(String accessToken) throws Exception {
        Claims claims = Jwts.parser()
        		.setSigningKey(secretKey.getBytes("UTF-8"))
        		.parseClaimsJws(accessToken).getBody();
        String userDetailsClaim = (String)claims.get("userDetails");
        UserDetails userDetails = JsonConverter.toObject(userDetailsClaim, UserDetails.class);
		return userDetails;
	}


}
