package net.oopscraft.application.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import net.oopscraft.application.core.TextTable;
import net.oopscraft.application.user.entity.User;
import net.oopscraft.application.util.StringUtility;

@Component
public class AuthenticationFilter extends GenericFilterBean   {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(AuthenticationFilter.class);
	private static final String AUTHORIZATION_HEADER = "Authorization";
	
	@Autowired
	AccessTokenEncoder accessTokenEncoder;
 
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
    	HttpServletRequest request = (HttpServletRequest) req;
        String uri = request.getRequestURI();
        String method = request.getMethod();
        LOGGER.debug(String.format("[%s][%s]",  method, uri));
        
        
        // print remember cookie for test
        if(request.getCookies() != null) {
	        for(Cookie cookie : request.getCookies()) {
	        	LOGGER.debug("Cookie:{}", new TextTable(cookie));
	        }
        }
        
        // JWT Token
        String authorization = request.getHeader(AUTHORIZATION_HEADER);
        if(StringUtility.isNotEmpty(authorization)) {
        	
            // decode principal
            try {
                String token = parseToken(authorization);
                LOGGER.debug(String.format("token:[%s]", token));
                User user = accessTokenEncoder.decode(token);
        		UserDetails userDetails = new UserDetails(user);
        		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        		SecurityContext securityContext = SecurityContextHolder.getContext();
        		securityContext.setAuthentication(authentication);
            }catch(Exception e) {
            	AuthenticationException authenticationException = new BadCredentialsException("Invalid Token Claims.");
            	LOGGER.error(authenticationException.getMessage());
            	throw authenticationException;
            }
        } 
        
        // forward
        chain.doFilter(req, res);
    }
	
	/**
	 * Parse token from authorization header
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

}
