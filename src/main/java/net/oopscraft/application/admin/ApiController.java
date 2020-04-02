package net.oopscraft.application.admin;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@PreAuthorize("hasAuthority('ADMN_API')")
@Controller
@RequestMapping("/admin/api")
public class ApiController {

	/**
	 * Forwards page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView index() throws Exception {
		ModelAndView modelAndView = new ModelAndView("admin/api.html");
		return modelAndView;
	}

}