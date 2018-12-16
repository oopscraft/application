package net.oopscraft.application.admin.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import net.oopscraft.application.core.JsonUtils;
import net.oopscraft.application.core.LocaleUtils;

@Controller
@RequestMapping("/admin/login")
public class LoginController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
	HttpServletRequest request;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView login() throws Exception {
		
		// detects locale
		Locale locale = request.getLocale();
		LOGGER.info("+ locale:" + locale);
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("admin/login.jsp");
		return modelAndView;
	}
	
	@RequestMapping(value="getLanguages", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String getLanguages() throws Exception {
		return JsonUtils.toJson(LocaleUtils.getLanguages());
	}

}
