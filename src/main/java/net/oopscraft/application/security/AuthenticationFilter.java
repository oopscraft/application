package net.oopscraft.application.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import net.oopscraft.application.utility.StringUtility;

public class AuthenticationFilter extends GenericFilterBean   {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(AuthenticationFilter.class);
	
	@Autowired
	AuthenticationProvider authenticationProvider;
 
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
    	HttpServletRequest request = (HttpServletRequest) req;
    	HttpServletResponse response = (HttpServletResponse) res;
        String uri = request.getRequestURI();
        String method = request.getMethod();
        LOGGER.debug("[{}][{}]",  method, uri);
        
        // JWT Token
        String accessToken = request.getHeader("X-Access-Token");
        if(StringUtils.isBlank(accessToken)) {
        	if(request.getCookies() != null) {
		        for(Cookie cookie : request.getCookies()) {
		        	if("X-Access-Token".equals(cookie.getName())) {
		        		accessToken = cookie.getValue();
		        	}
		        }
        	}
        }
        
        // decode principal
        if(StringUtility.isNotEmpty(accessToken)) {
            try {
                LOGGER.debug(String.format("token:[%s]", accessToken));
        		UserDetails userDetails = authenticationProvider.decodeAccessToken(accessToken);
        		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        		SecurityContext securityContext = SecurityContextHolder.getContext();
        		securityContext.setAuthentication(authentication);
        		
        		// keep alive
        		accessToken = authenticationProvider.encodeAccessToken(userDetails);
    			response.setHeader("X-Access-Token", accessToken);
    			Cookie cookie = new Cookie("X-Access-Token", accessToken);
    			cookie.setPath("/");
    			cookie.setHttpOnly(true);
    			response.addCookie(cookie);
        		
            }catch(Exception ignore) {
            	LOGGER.warn("Invalid Authentication Token{}[{{}]", accessToken);
            }
        } 
        
        // forward
        chain.doFilter(req, res);
    }

}
