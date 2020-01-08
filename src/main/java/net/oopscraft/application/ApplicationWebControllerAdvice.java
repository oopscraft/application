package net.oopscraft.application;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import net.oopscraft.application.core.JsonConverter;
import net.oopscraft.application.core.PageInfo;
import net.oopscraft.application.core.ValueMap;
import net.oopscraft.application.user.UserRepository;
import net.oopscraft.application.user.entity.User;

@Controller
@ControllerAdvice
@RequestMapping("/")
public class ApplicationWebControllerAdvice {
	
	private static Logger LOGGER = LoggerFactory.getLogger(ApplicationWebControllerAdvice.class);
	
	@Autowired
	UserRepository userRepository;
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView index(User user, PageInfo pageInfo) throws Exception {
		ModelAndView modelAndView = new ModelAndView("admin/__admin.html");
		LOGGER.info(JsonConverter.toJson(user));
		LOGGER.info(JsonConverter.toJson(pageInfo));
		return modelAndView;
	}
	
	@RequestMapping(value = "getUsers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody List<User> getUsers(User user, PageInfo pageInfo) throws Exception {
		LOGGER.info(JsonConverter.toJson(user));
		LOGGER.info(JsonConverter.toJson(pageInfo));
		List<User> users = userRepository.findAll();
		LOGGER.info(JsonConverter.toJson(users));
		return users;
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
