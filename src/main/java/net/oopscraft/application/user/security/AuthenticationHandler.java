package net.oopscraft.application.user.security;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import net.oopscraft.application.user.UserLogin;
import net.oopscraft.application.user.dao.UserLoginRepository;
import net.oopscraft.application.user.entity.User;

@Component
public class AuthenticationHandler implements AuthenticationSuccessHandler, AuthenticationFailureHandler, AuthenticationEntryPoint, AccessDeniedHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationHandler.class);

	@Autowired
	@Lazy
	HttpServletRequest request;
	
	@Autowired
	MessageSource messageSource;
	
	@Autowired
	LocaleResolver localeResolver;
	
	@Autowired
	AccessTokenEncoder accessTokenEncoder;
	
	@Autowired
	UserLoginRepository userLoginRepository;
	
	/**
	 * On authentication is success.
	 * @param HttpServletRequest, HttpServletResponse, Authentication
	 * @return void
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		
		// Remember me
		Cookie cookie = new Cookie("cookie_value", "cookie_value");
	    cookie.setPath("/");
		cookie.setMaxAge(60*3);
		response.addCookie(cookie);
		
		// issue JWT access token.
		try {
			if(authentication.getPrincipal() instanceof UserDetails) {
				UserDetails userDetails = (UserDetails)authentication.getPrincipal();
				response.setHeader("Authorization", "Bearer " + accessTokenEncoder.encode(userDetails.getUser()));
			}
		}catch(Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new IOException(e);
		}
		
		// Saves Login History
		UserLogin userLogin = new UserLogin();
        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();
		userLogin.setUserId(user.getId());
		userLogin.setDate(new Date());
		userLogin.setSuccessYn("Y");
		userLogin.setFailReason(null);
		userLogin.setIp(request.getRemoteAddr());
		userLogin.setAgent(request.getHeader("User-Agent"));
		userLogin.setReferer(request.getHeader("referer"));
		userLoginRepository.saveAndFlush(userLogin);

		// sets response header
		response.setStatus(HttpServletResponse.SC_OK);
	}
	
	/**
	 * On authentication is failed.
	 * @param HttpServletRequest, HttpServletResponse, AuthenticationException
	 * @return void
	 */
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
		String message = null;
		Locale locale = localeResolver.resolveLocale(request);
		if(exception instanceof UsernameNotFoundException) {
			String item = messageSource.getMessage("application.text.user", null, locale);
			message = messageSource.getMessage("application.message.itemNotFound", new Object[]{ item }, locale);
		}else if(exception instanceof BadCredentialsException) {
			String item = messageSource.getMessage("application.text.password", null, locale);
			message = messageSource.getMessage("application.message.invalidItem", new Object[]{ item }, locale);
		}else {
			message = exception.getMessage();
		}
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		OutputStream out = response.getOutputStream();
		out.write(message.getBytes());
		
		// Saves Login History
		if(exception instanceof UsernameNotFoundException == false) {
			UserLogin userLogin = new UserLogin();
			userLogin.setUserId(request.getParameter("id"));
			userLogin.setDate(new Date());
			userLogin.setSuccessYn("N");
			userLogin.setFailReason(message);
			userLogin.setIp(request.getRemoteAddr());
			userLogin.setAgent(request.getHeader("User-Agent"));
			userLogin.setReferer(request.getHeader("referer"));
			userLoginRepository.saveAndFlush(userLogin);
		}
	}

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
