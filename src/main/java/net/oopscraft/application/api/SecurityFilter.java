package net.oopscraft.application.api;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import net.oopscraft.application.core.JsonConverter;
import net.oopscraft.application.user.Authority;
import net.oopscraft.application.user.User;

public class SecurityFilter implements Filter {
	
	private static Logger LOGGER = LoggerFactory.getLogger(SecurityFilter.class);
	private static final String AUTHORIZATION_HEADER = "Authorization";
	
	@Value("${secretKey}")
	String secretKey;
 
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
    	HttpServletRequest request = (HttpServletRequest) req;
    	HttpServletResponse response = (HttpServletResponse) res;
        String uri = request.getRequestURI();
        String method = request.getMethod();
        LOGGER.debug(String.format("[%s][%s]",  method, uri));
        
        // 토큰 디코딩
        Claims claims = null;
        try {
            String authorization = request.getHeader(AUTHORIZATION_HEADER);
            String token = parseToken(authorization);
        	claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        }catch(Exception e) {
        	AuthenticationException authenticationException = new BadCredentialsException("Invalid Token.");
        	LOGGER.error(authenticationException.getMessage());
        	throw authenticationException;
        }
        
        // De-serialize principal
        try {
        	String id = (String)claims.get("id");
        	String password = (String)claims.get("password");
        	String authorities = (String)claims.get("authorities");
        	User user = new User();
        	user.setId(id);
        	user.setPassword(password);
        	List<Authority> authorityList= JsonConverter.convertJsonToObjectList(authorities, Authority.class);
        	user.setAuthorities(authorityList);
        	SecurityContext securityContext = SecurityContextHolder.getContext();
        	Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
        	securityContext.setAuthentication(authentication);
        }catch(Exception e) {
        	AuthenticationException authenticationException = new BadCredentialsException("Invalid Token Claims.");
        	LOGGER.error(authenticationException.getMessage());
        	throw authenticationException;
        }
        
        // forward
        chain.doFilter(req, res);
    }
 
    @Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Authorization 커스텀해더 값에서 JWT토큰 값 추출
	 * @param authorization
	 * @return
	 */
	private String parseToken(String authorization) {
        if(authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7, authorization.length());
            return token;
        }
        return null;
	}
	
	/**
	 * 토큰 Validation 체크
	 * @param token
	 * @return
	 */
	private boolean validateToken(String token) {
       try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            LOGGER.info("Invalid JWT signature: " + e.getMessage());
            LOGGER.debug("Exception " + e.getMessage());
            return false;
        }

	}

}
