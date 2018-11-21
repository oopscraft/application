package net.oopscraft.application.admin;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin/dash")
public class DashController {
	
	@RequestMapping(method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ModelAndView dash() throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("admin/dash.jsp");
		return modelAndView;
	}

}
