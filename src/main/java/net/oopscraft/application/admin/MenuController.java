package net.oopscraft.application.admin;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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

import net.oopscraft.application.core.Pagination;
import net.oopscraft.application.menu.Menu;
import net.oopscraft.application.menu.MenuService;
import net.oopscraft.application.security.SecurityPolicy;


@PreAuthorize("hasAuthority('ADMN_MENU')")
@Controller
@RequestMapping("/admin/menu")
public class MenuController {

	@Autowired
	MenuService menuService;

	/**
	 * Forwards user page
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView index() throws Exception {
		ModelAndView modelAndView = new ModelAndView("admin/menu.html");
		modelAndView.addObject("LinkTarget", Menu.LinkTarget.values());
		modelAndView.addObject("SecurityPolicy", SecurityPolicy.values());
		return modelAndView;
	}
	
	/**
	 * Returns menu list
	 * @param menu
	 * @param pagination
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getMenus", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public List<Menu> getMenus(@ModelAttribute Menu menu, @ModelAttribute Pagination pagination, HttpServletResponse response) throws Exception {
		pagination.setEnableTotalCount(true);
		List<Menu> menus = menuService.getMenus(menu, pagination);
		response.setHeader(HttpHeaders.CONTENT_RANGE, pagination.getContentRange());
		return menus;
	}
	
	/**
	 * Returns menu
	 * @param menu
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getMenu", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public Menu getMenu(@ModelAttribute Menu menu) throws Exception {
		return menuService.getMenu(menu.getId());
	}

	/**
	 * Saves menu
	 * @param menu
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('ADMN_MENU_EDIT')")
	@RequestMapping(value = "saveMenu", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public Menu saveMenu(@RequestBody @Valid Menu menu) throws Exception {
		return menuService.saveMenu(menu);
	}
	
	/**
	 * Deletes menu
	 * @param menu
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('ADMN_MENU_EDIT')")
	@RequestMapping(value = "deleteMenu", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public void deleteMenu(@RequestBody Menu menu) throws Exception {
		menuService.deleteMenu(menu);
	}
	
	/**
	 * Changes menu upper id
	 * @param menu
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('ADMN_MENU_EDIT')")
	@RequestMapping(value = "changeUpperId", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public Menu changeUpperId(@RequestBody Menu menu) throws Exception {
		return menuService.changeUpperId(menu.getId(), menu.getUpperId());
	}

}
