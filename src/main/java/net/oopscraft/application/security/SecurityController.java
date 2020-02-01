package net.oopscraft.application.security;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/security")
public class SecurityController {
	
	@RequestMapping(value = "login", method = RequestMethod.GET)
	public ModelAndView login() throws Exception {
		ModelAndView modelAndView = new ModelAndView("security/login.html");
		return modelAndView;
	}
	
	
	
}
