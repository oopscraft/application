package net.oopscraft.application.security;

import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import net.oopscraft.application.core.JsonConverter;
import net.oopscraft.application.user.entity.Authority;
import net.oopscraft.application.user.entity.User;

@Component
public class AccessTokenEncoder {
	
	private static String secret = "secret";
	
	/**
	 * encode
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public String encode(User user) throws Exception {
		String jwt = Jwts.builder()
				  .setExpiration(new Date(System.currentTimeMillis() + 100000))
				  .claim("id", user.getId())
				  .claim("name", user.getName())
				  .claim("nickName", user.getNickname())
				  .claim("status", user.getStatus())
				  .claim("authorities", JsonConverter.toJson(user.getAuthorities()))
				  .signWith(SignatureAlgorithm.HS256, secret.getBytes("UTF-8"))
				  .compact();
		return jwt;
	}
	
	/**
	 * decode
	 * @param token
	 * @return
	 * @throws Exception
	 */
	public User decode(String token) throws Exception {
        Claims claims = Jwts.parser()
        		.setSigningKey(secret.getBytes("UTF-8"))
        		.parseClaimsJws(token).getBody();
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
