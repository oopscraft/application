package net.oopscraft.application.page.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import net.oopscraft.application.layout.Layout;
import net.oopscraft.application.layout.LayoutService;
import net.oopscraft.application.page.Page;


@Controller
@RequestMapping("/page")
public class PageController {

	@Autowired
	HttpServletResponse response;
	
	@Autowired
	LayoutService layoutService;

	/**
	 * list
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="{pageId}", method = RequestMethod.GET)
	public ModelAndView list(@PathVariable("boardId")String boardId) throws Exception {
		Layout layout = layoutService.getAvailableLayout(null);
		ModelAndView modelAndView = new ModelAndView("page/__view.tiles");
		modelAndView.addObject("layout", layout);
		Page page = new Page();
		modelAndView.addObject("page", page);
		return modelAndView;
	}

}