package net.oopscraft.application.admin;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin/sign")
public class SignController implements AuthenticationSuccessHandler, AuthenticationFailureHandler {

	@RequestMapping(method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ModelAndView sign() throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("admin/sign.jsp");
		return modelAndView;
	}

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
