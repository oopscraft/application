package net.oopscraft.application;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
@Controller
@RequestMapping("/")
public class ApplicationControllerAdvice {
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView index() throws Exception {
		ModelAndView modelAndView = new ModelAndView("main.tiles");
		return modelAndView;
	}
	
    @ModelAttribute("__config")
    public Map<String,String> getConfig() throws Exception {
    	return ApplicationContainer.getApplication().getConfig();
    }

}
