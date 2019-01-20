package net.oopscraft.application.admin.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import net.oopscraft.application.Application;
import net.oopscraft.application.ApplicationContainer;

@Controller
@RequestMapping("/admin/login")
public class LoginController {
	
	@Autowired
	HttpServletRequest request;
	
	@Autowired
	ApplicationContext context;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView login() throws Exception {
		ModelAndView modelAndView = new ModelAndView();
//		context.getMessage("akakak", null, Locale.getDefault());
		modelAndView.setViewName("admin/login.jsp");
		return modelAndView;
	}

}
