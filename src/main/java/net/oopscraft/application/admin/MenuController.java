package net.oopscraft.application.admin;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import net.oopscraft.application.core.PageInfo;
import net.oopscraft.application.menu.MenuService;
import net.oopscraft.application.menu.entity.Menu;
import net.oopscraft.application.user.entity.Group;


//@PreAuthorize("hasAuthority('ADMIN_MENU')")
@Controller
@RequestMapping("/admin/menu")
public class MenuController {

	@Autowired
	HttpServletResponse response;

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
		return modelAndView;
	}
	
	/**
	 * Returns menu list
	 * @param menu
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getMenus", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public List<Menu> getMenus(@ModelAttribute Menu menu, @ModelAttribute PageInfo pageInfo) throws Exception {
		pageInfo.setEnableTotalCount(true);
		List<Menu> menus = menuService.getMenus(menu, pageInfo);
		response.setHeader(HttpHeaders.CONTENT_RANGE, pageInfo.getContentRange());
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
	@RequestMapping(value = "saveMenu", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public Menu saveMenu(@RequestBody Menu menu) throws Exception {
		return menuService.saveMenu(menu);
	}
	
	/**
	 * Deletes menu
	 * @param menu
	 * @throws Exception
	 */
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
	@RequestMapping(value = "changeUpperId", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public Menu changeUpperId(@RequestBody Menu menu) throws Exception {
		return menuService.changeUpperId(menu.getId(), menu.getUpperId());
	}

}
