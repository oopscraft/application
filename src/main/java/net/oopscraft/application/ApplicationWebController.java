package net.oopscraft.application;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import net.oopscraft.application.core.ValueMap;
import net.oopscraft.application.security.UserDetails;
import net.oopscraft.application.user.entity.Authority;
import net.oopscraft.application.user.entity.User;

@Controller
@ControllerAdvice
@RequestMapping("/")
public class ApplicationWebController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationWebController.class);
	
	/**
	 * Default exception handler
	 * @param request
	 * @param response
	 * @param exception
	 * @return
	 * @throws Exception
	 */
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public ValueMap handleException(HttpServletRequest request, HttpServletResponse response, Exception exception) throws Exception {
		LOGGER.error(exception.getMessage(), exception);
		String contentType = request.getHeader("Content-Type");
		if(contentType != null && MediaType.APPLICATION_JSON_VALUE.startsWith(contentType)){
			ValueMap responseMap = new ValueMap();
			responseMap.set("exception", exception.getClass().getName());
			responseMap.set("message", exception.getMessage());
			responseMap.set("stackTrace", ExceptionUtils.getRootCauseMessage(exception));
			return responseMap;
		}else{
			throw exception;
		}
	}

	/**
	 * Returns application configure
	 * @return
	 * @throws Exception
	 */
    @ModelAttribute("_configure")
    public ValueMap getConfigure() throws Exception {
    	ValueMap configure = new ValueMap();
    	return configure;
    }
	
	@ModelAttribute("__user")
	public User getUser() throws Exception {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authentication = securityContext.getAuthentication();
		
		// Anonymous user
		if(authentication instanceof AnonymousAuthenticationToken) {
			return new User();
		}

		// Login user
		if(authentication.getPrincipal() instanceof UserDetails) {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			return userDetails.getUser();
		}
		
		// JUNIT Mock Test user
		if(authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
			org.springframework.security.core.userdetails.User springUser = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
			User user = new User();
			user.setId(springUser.getUsername());
			for(org.springframework.security.core.GrantedAuthority grantedAuthority : springUser.getAuthorities()) {
				grantedAuthority.getAuthority();
				Authority authority = new Authority();
				authority.setId(grantedAuthority.getAuthority());
				user.addAuthority(authority);
			}
			return user;
		}

		// return null user
		return new User();
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView index() throws Exception {
		ModelAndView modelAndView = new ModelAndView("application.html");
		modelAndView.addObject("THEME","application");
		return modelAndView;
	}
	
	/**
	 * Login page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "login", method = RequestMethod.GET)
	public ModelAndView login() throws Exception {
		ModelAndView modelAndView = new ModelAndView("login.html");
		return modelAndView;
	}
	



	

	
	
	
	
	
	
	
	
	
	
	
	

	
//    @ModelAttribute("__configuration")
//    public Map<String,String> getConfiguration() throws Exception {
//    	return ApplicationContainer.getApplication().getConfiguration();
//    }
	
//	@ModelAttribute("__user")
//	public User getUser() throws Exception {
//		SecurityContext securityContext = SecurityContextHolder.getContext();
//		Authentication authentication = securityContext.getAuthentication();
//		
//		// Anonymous user
//		if(authentication instanceof AnonymousAuthenticationToken) {
//			return new User();
//		}
//
//		// Login user
//		if(authentication.getPrincipal() instanceof UserDetails) {
//			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//			return userDetails.getUser();
//		}
//		
//		// JUNIT Mock Test user
//		if(authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
//			org.springframework.security.core.userdetails.User springUser = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
//			User user = new User();
//			user.setId(springUser.getUsername());
//			for(GrantedAuthority grantedAuthority : springUser.getAuthorities()) {
//				grantedAuthority.getAuthority();
//				Authority authority = new Authority();
//				authority.setId(grantedAuthority.getAuthority());
//				user.addAuthority(authority);
//			}
//			return user;
//		}
//
//		// return null user
//		return new User();
//	}

}
