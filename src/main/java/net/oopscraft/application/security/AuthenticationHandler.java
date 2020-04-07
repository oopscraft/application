package net.oopscraft.application.security;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.servlet.LocaleResolver;

import net.oopscraft.application.user.UserLoginRepository;
import net.oopscraft.application.user.entity.UserLogin;

public class AuthenticationHandler implements AuthenticationSuccessHandler, AuthenticationFailureHandler, AuthenticationEntryPoint, LogoutSuccessHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationHandler.class);

	@Autowired
	@Lazy
	HttpServletRequest request;
	
	@Autowired
	MessageSource messageSource;
	
	@Autowired
	LocaleResolver localeResolver;
	
	@Autowired
	UserLoginRepository userLoginRepository;
	
	@Autowired
	AuthenticationProvider authenticationProvider;
	
	/**
	 * On authentication is success.
	 * @param HttpServletRequest, HttpServletResponse, Authentication
	 * @return void
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		try {
	        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			// issue JWT access token.
	        String accessToken = authenticationProvider.encodeAccessToken(userDetails);
			response.setHeader("X-Access-Token", accessToken);
			Cookie cookie = new Cookie("X-Access-Token", accessToken);
			cookie.setPath("/");
			cookie.setHttpOnly(true);
			response.addCookie(cookie);
			
			// sets user default locale
			if(StringUtils.isNotEmpty(userDetails.getLanguage())){
				localeResolver.setLocale(request, response, Locale.forLanguageTag(userDetails.getLanguage()));
			}
			
			// Saves Login History
			UserLogin userLogin = new UserLogin();
			userLogin.setUserId(userDetails.getUsername());
			userLogin.setDate(new Date());
			userLogin.setSuccessYn("Y");
			userLogin.setFailReason(null);
			userLogin.setIp(request.getRemoteAddr());
			userLogin.setAgent(request.getHeader("User-Agent"));
			userLogin.setReferer(request.getHeader("referer"));
			userLoginRepository.saveAndFlush(userLogin);
			
			// sets response header
			response.setStatus(HttpServletResponse.SC_OK);
			
		}catch(Exception e) {
			LOGGER.warn(e.getMessage(), e);
			throw new ServletException(e);
		}
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
			String item = messageSource.getMessage("application.user", null, locale);
			message = messageSource.getMessage("application.global.itemNotFound", new Object[]{ item }, locale);
		}else if(exception instanceof BadCredentialsException) {
			String item = messageSource.getMessage("application.user.password", null, locale);
			message = messageSource.getMessage("application.global.itemNotMatch", new Object[]{ item }, locale);
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
	
	/**
	 * logout
	 */
	@Override 
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		if(request.getCookies() != null) {
	        for(Cookie cookie : request.getCookies()) {
	        	if("X-Access-Token".equals(cookie.getName())) {
	        		cookie.setPath("/");
	        		cookie.setValue("");
	        		cookie.setMaxAge(-1);
	        		response.addCookie(cookie);
	        	}
	        }
		}
	}

	/*
	 * AuthenticationEntryPoint.commence
	 */
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
    	LOGGER.warn(authException.getMessage());
    	// in case of AJAX request
    	if("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))){
        	response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        	response.getWriter().flush();
    	} else {
    		response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
    		if(request.getRequestURI().startsWith("/admin")){
    			response.setHeader("Location", "/admin/login");
    		}else {
    			response.setHeader("Location", "/user/login");
    		}
    		response.getWriter().flush();
    	}
	}

}
