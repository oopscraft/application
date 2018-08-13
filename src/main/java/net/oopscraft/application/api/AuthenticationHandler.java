package net.oopscraft.application.api;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

public class AuthenticationHandler implements AuthenticationEntryPoint, AccessDeniedHandler {
	
	private static Logger LOGGER = LoggerFactory.getLogger(SecurityFilter.class);

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
    	LOGGER.warn(authException.getMessage());
    	response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    	response.getWriter().write("Invalid Token Claims.");
    	response.getWriter().flush();
	}
	
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
		LOGGER.warn(accessDeniedException.getMessage());
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    	response.getWriter().write("Unauthorized.");
    	response.getWriter().flush();
	}

}
