package net.oopscraft.application;

import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import net.oopscraft.application.common.JsonConverter;
import net.oopscraft.application.common.PageInfo;
import net.oopscraft.application.common.ValueMap;
import net.oopscraft.application.user.UserRepository;
import net.oopscraft.application.user.entity.User;

@Controller
@ControllerAdvice
@RequestMapping("/")
public class ApplicationWebControllerAdvice {
	
	private static Logger LOGGER = LoggerFactory.getLogger(ApplicationWebControllerAdvice.class);
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public ValueMap handleException(Exception e) throws Exception {
		LOGGER.error(e.getMessage(), e);
		ValueMap responseMap = new ValueMap();
		responseMap.set("exception", e.getClass().getName());
		responseMap.set("message", e.getMessage());
		responseMap.set("stackTrace", ExceptionUtils.getRootCauseMessage(e));
		return responseMap;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView index() throws Exception {
		ModelAndView modelAndView = new ModelAndView("admin/__admin.html");
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
