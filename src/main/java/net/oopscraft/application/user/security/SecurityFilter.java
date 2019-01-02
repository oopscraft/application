package net.oopscraft.application.user.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

@Component
public class SecurityFilter extends GenericFilterBean   {
	
	private static Logger LOGGER = LoggerFactory.getLogger(SecurityFilter.class);
	
	@Autowired
	SecurityHandler securityHandler;
 
	@Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
    	HttpServletRequest request = (HttpServletRequest) req;
    	HttpServletResponse response = (HttpServletResponse) res;
        String uri = request.getRequestURI();
        String method = request.getMethod();
        LOGGER.info(String.format("[%s][%s]",  method, uri));
        
        /*
        if(1 == 1) {
        	securityHandler.handle(request, response, new AccessDeniedException("FDSAFDSAFDSA"));
        	return;
        }
        */
        
        chain.doFilter(req, res);        	
        

    }
 


	


}
