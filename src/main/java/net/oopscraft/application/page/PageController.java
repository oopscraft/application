package net.oopscraft.application.page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/page")
public class PageController {
	
	@Autowired
	PageService pageService;
	
	/**
	 * index
	 * @param boardId
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("this.hasReadAuthority(#id)")
	@RequestMapping(value="{id}", method = RequestMethod.GET)
	public ModelAndView index(@PathVariable("id") String id) throws Exception {
		ModelAndView modelAndView = new ModelAndView("page/page.html");
		Page page = pageService.getPage(id);
		modelAndView.addObject("page", page);
		return modelAndView;
	}

	/**
	 * hasReadAuthority
	 * @param id
	 * @param userDetails
	 * @return
	 * @throws Exception
	 */
	public boolean hasReadAuthority(String id) throws Exception {
		Page page = pageService.getPage(id);
		return page.hasReadAuthority();
	}

}
