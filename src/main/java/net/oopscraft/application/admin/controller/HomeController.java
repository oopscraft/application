package net.oopscraft.application.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin")
public class HomeController {
	
	@RequestMapping(value="")
	public ModelAndView home() throws Exception {
		ModelAndView modelAndView = new ModelAndView("redirect:/admin/monitor");
		return modelAndView;
	}
	
}
