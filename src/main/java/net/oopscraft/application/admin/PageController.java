package net.oopscraft.application.admin;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.annotation.JsonView;

import net.oopscraft.application.core.Pagination;
import net.oopscraft.application.message.Message;
import net.oopscraft.application.message.MessageI18n;
import net.oopscraft.application.page.Page;
import net.oopscraft.application.page.PageI18n;
import net.oopscraft.application.page.PageService;
import net.oopscraft.application.security.SecurityPolicy;



@PreAuthorize("hasAuthority('ADMN_PAGE')")
@Controller
@RequestMapping("/admin/page")
public class PageController {

	@Autowired
	PageService pageService;

	/**
	 * Forwards page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView index() throws Exception {
		ModelAndView modelAndView = new ModelAndView("admin/page.html");
		modelAndView.addObject(Page.Format.class.getSimpleName(), Page.Format.values());
		modelAndView.addObject(SecurityPolicy.class.getSimpleName(), SecurityPolicy.values());
		return modelAndView;
	}
	
	/**
	 * Returns pages
	 * @param role
	 * @param pagination
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getPages", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@JsonView(List.class)
	public List<Page> getPages(
		 @ModelAttribute Page page
		,Pagination pagination
		,HttpServletResponse response
	) throws Exception {
		pagination.setEnableTotalCount(true);
		List<Page> properties = pageService.getPages(page, pagination);
		pagination.setContentRangeHeader(response);
		return properties;
	}
	
	/**
	 * Returns Property
	 * @param Property
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getPage", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public Page getPage(@ModelAttribute Page page) throws Exception {
		return pageService.getPage(page.getId());
	}

	/**
	 * Saves Property
	 * @param Property
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('ADMN_PAGE_EDIT')")
	@RequestMapping(value = "savePage", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional
	public Page savePage(@RequestBody @Valid Page page) throws Exception {
		return pageService.savePage(page);
	}
	
	/**
	 * Deletes Property
	 * @param Property
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('ADMN_PAGE_EDIT')")
	@RequestMapping(value = "deletePage", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional
	public void deletePage(@RequestBody Page page) throws Exception {
		pageService.deletePage(page);
	}
	
	/**
	 * getMessageI18ns
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('ADMN_PAGE_EDIT')")
	@RequestMapping(value = "getPageI18ns", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public List<PageI18n> getPageI18ns(@ModelAttribute Page page) throws Exception {
		return pageService.getPageI18ns(page.getId());
	}
	
	/**
	 * saveMessageI18ns
	 * @param page
	 * @param messageI18ns
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('ADMN_PAGE_EDIT')")
	@RequestMapping(value = "savePageI18ns", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional
	public List<PageI18n> savePageI18ns(@ModelAttribute Page page, @RequestBody List<PageI18n> pageI18ns) throws Exception {
		return pageService.savePageI18ns(page.getId(), pageI18ns);
	}

}