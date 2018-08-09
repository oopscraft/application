package net.oopscraft.application.user;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class AuthenticationHandler implements AuthenticationSuccessHandler, AuthenticationFailureHandler {
	
	/**
	 * 인증 성공시 호출 핸들러
	 * @param HttpServletRequest, HttpServletResponse, Authentication
	 * @return void
	 */
	@Override
	public void onAuthenticationSuccess(
		 HttpServletRequest request
		,HttpServletResponse response
		,Authentication authentication
	) throws IOException, ServletException {
		String referer = request.getHeader("referer");
		if(referer != null && referer.isEmpty() == true) {
			response.sendRedirect(referer);	
		}else {
			response.sendRedirect("/");
		}
	}
	
	/**
	 * 인증 실패시 호출 핸들러
	 * @param HttpServletRequest, HttpServletResponse, AuthenticationException
	 * @return void
	 */
	@Override
	public void onAuthenticationFailure(
		HttpServletRequest request, 
		HttpServletResponse response,
		AuthenticationException exception
	) throws IOException, ServletException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		OutputStream out = response.getOutputStream();
		out.write(exception.getMessage().getBytes());
	}



}
