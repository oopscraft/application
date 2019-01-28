package net.oopscraft.application;

import java.util.Map;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import net.oopscraft.application.user.User;
import net.oopscraft.application.user.security.UserDetails;

@ControllerAdvice
@Controller
@RequestMapping("/")
public class ApplicationControllerAdvice {
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView index() throws Exception {
		ModelAndView modelAndView = new ModelAndView("main.tiles");
		return modelAndView;
	}
	
	@ModelAttribute("__user")
	public User getUser() throws Exception {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authentication = securityContext.getAuthentication();
		if(authentication instanceof AnonymousAuthenticationToken) {
			return new User();
		}
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		return userDetails.getUser();
	}
	
    @ModelAttribute("__config")
    public Map<String,String> getConfig() throws Exception {
    	return ApplicationContainer.getApplication().getConfig();
    }

}
