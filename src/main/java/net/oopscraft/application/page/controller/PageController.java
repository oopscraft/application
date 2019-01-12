package net.oopscraft.application.page.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import net.oopscraft.application.page.Page;
import net.oopscraft.application.page.PageService;

@Controller
@RequestMapping("/page")
public class PageController {
	
	@Autowired
	PageService pageService;
	
	@RequestMapping(value="{id}", method = RequestMethod.GET)
	public ModelAndView list(@PathVariable("id")String id) throws Exception {
		Page page = pageService.getPage(id);
		ModelAndView modelAndView = new ModelAndView("page/page.tiles");
		modelAndView.addObject("page", page);
		return modelAndView;
	}
	
}
