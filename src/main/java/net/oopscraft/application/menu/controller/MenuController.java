package net.oopscraft.application.menu.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import net.oopscraft.application.menu.Menu;
import net.oopscraft.application.menu.MenuService;

@Controller
@RequestMapping("/menu")
public class MenuController {
	
	@Autowired
	MenuService menuService;
	
	@Autowired
	HttpServletRequest request;
	
	@RequestMapping(value="{id}", method = RequestMethod.GET)
	public ModelAndView list(@PathVariable("id")String id) throws Exception {
		Menu menu = menuService.getMenu(id);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("menu", menu);
		switch(menu.getType()) {
			case LINK :
				modelAndView.setView(new RedirectView(menu.getValue()));
			break;
			default:
				modelAndView.setViewName("menu/menu.tiles");
		}
		return modelAndView;
	}
	
}
