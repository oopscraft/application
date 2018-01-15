/*
 * Copyright since 2002 oopscraft.net
 *
 * Everyone is permitted to copy and distribute verbatim copies of this license document, 
 * but changing it is not allowed.
 * Released under the LGPL-3.0 licence
 * https://opensource.org/licenses/lgpl-3.0.html
 */
package net.oopscraft.application.console;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/console/login")
public class LoginController {
	
	
	@RequestMapping(value="", method=RequestMethod.GET) 
	public ModelAndView getLogin() throws Exception {
		return new ModelAndView("console/login.jsp");
	}
	
	@RequestMapping(value="", method=RequestMethod.POST)
	public ModelAndView postLogin() throws Exception {
		return new ModelAndView("redirect:/console");
	}

}
