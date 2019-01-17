package net.oopscraft.application.admin.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import net.oopscraft.application.core.JsonUtils;
import net.oopscraft.application.core.PageInfo;
import net.oopscraft.application.core.StringUtils;
import net.oopscraft.application.page.Page;
import net.oopscraft.application.page.PageService;
import net.oopscraft.application.page.PageService.PageSearchType;

@PreAuthorize("hasAuthority('ADMIN_PAGE')")
@Controller
@RequestMapping("/admin/page")
public class PageController {

	@Autowired
	PageService pageService;

	@Autowired
	HttpServletResponse response;

	/**
	 * Page main
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView main() throws Exception {
		ModelAndView modelAndView = new ModelAndView("admin/page.tiles");
		modelAndView.addObject("types", Page.Type.values());
		modelAndView.addObject("policies", Page.Policy.values());
		return modelAndView;
	}

	/**
	 * Gets pages
	 * 
	 * @param searchKey
	 * @param searchValue
	 * @param page
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getPages", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String getBoards(
		@RequestParam(value = "page") Integer page,
		@RequestParam(value = "rows")Integer rows,
		@RequestParam(value = "searchType", required = false) String searchType,
		@RequestParam(value = "searchValue", required = false) String searchValue
	) throws Exception {
		PageInfo pageInfo = new PageInfo(page, 20, true);
		PageSearchType pageSearchType = null;
		if(StringUtils.isNotEmpty(searchType)) {
			pageSearchType = PageSearchType.valueOf(searchType);
		}
		List<Page> pages = pageService.getPages(pageInfo, pageSearchType, searchValue);
		response.setHeader(HttpHeaders.CONTENT_RANGE, pageInfo.getContentRange());
		return JsonUtils.toJson(pages);
	}

	/**
	 * Gets board
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getPage", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String getPage(@RequestParam(value = "id") String id) throws Exception {
		Page page = pageService.getPage(id);
		return JsonUtils.toJson(page);
	}

	/**
	 * Saves page
	 * 
	 * @param payload
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "savePage", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public void savePage(@RequestBody String payload) throws Exception {
		Page page = JsonUtils.toObject(payload, Page.class);
		pageService.savePage(page);
	}

	/**
	 * Deletes page
	 * 
	 * @param payload
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "deletePage", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public void deletePage(@RequestParam(value = "id") String id) throws Exception {
		pageService.deletePage(id);
	}

}