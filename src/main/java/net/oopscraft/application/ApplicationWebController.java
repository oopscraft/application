package net.oopscraft.application;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@ControllerAdvice
@RequestMapping("/")
public class ApplicationWebController {
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView index() throws Exception {
		ModelAndView modelAndView = new ModelAndView("main.tiles");
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