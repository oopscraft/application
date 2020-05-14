package net.oopscraft.application.page;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import net.oopscraft.application.board.Board;
import net.oopscraft.application.security.SecurityPolicyEvaluator;
import net.oopscraft.application.security.UserDetails;

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
	@RequestMapping(value="{id}", method = RequestMethod.GET)
	public ModelAndView index(
		 @PathVariable("id") String id
		,@AuthenticationPrincipal UserDetails userDetails
	) throws Exception {
		ModelAndView modelAndView = new ModelAndView("page/page.html");
		Page page = pageService.getPage(id);
//		SecurityPolicyEvaluator.checkPolicyAuthority(board.getAccessPolicy(), board.getAccessAuthorities(), userDetails);
		modelAndView.addObject("page", page);
		return modelAndView;
	}
	
	/**
	 * page
	 */
	@RequestMapping(value = "/**", method = RequestMethod.GET)
	public String forwardPage(HttpServletRequest request) throws Exception {
		String resource = request.getRequestURI();
		String resourceForward = String.format("forward:/WEB-INF/%s", resource);
		return resourceForward;
	}

}
