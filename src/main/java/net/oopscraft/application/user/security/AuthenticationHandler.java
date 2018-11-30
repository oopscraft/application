package net.oopscraft.application.user.security;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

@Component
public class AuthenticationHandler implements AuthenticationSuccessHandler, AuthenticationFailureHandler {

	@Autowired
	HttpServletRequest request;
	
	@Autowired
	MessageSource messageSource;
	
	@Autowired
	LocaleResolver localeResolver;
	
	/**
	 * 인증 성공시 호출 핸들러
	 * @param HttpServletRequest, HttpServletResponse, Authentication
	 * @return void
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		response.setStatus(HttpServletResponse.SC_OK);
		OutputStream out = response.getOutputStream();
		String referer = request.getHeader("referer");
		if(referer != null && referer.isEmpty() == true) {
			out.write(referer.getBytes());
		}else {
			out.write("/admin".getBytes());
		}
	}
	
	/**
	 * 인증 실패시 호출 핸들러
	 * @param HttpServletRequest, HttpServletResponse, AuthenticationException
	 * @return void
	 */
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
		String message = null;
		if(exception instanceof UsernameNotFoundException) {
			message = messageSource.getMessage("message.userNotFound", null, localeResolver.resolveLocale(request));
		}else if(exception instanceof BadCredentialsException) {
			message = messageSource.getMessage("message.passwordIncorrect", null, localeResolver.resolveLocale(request));
		}else {
			message = exception.getMessage();
		}
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		OutputStream out = response.getOutputStream();
		out.write(message.getBytes());
	}
	

}
