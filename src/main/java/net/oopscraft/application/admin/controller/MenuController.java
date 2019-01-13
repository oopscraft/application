package net.oopscraft.application.admin.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import net.oopscraft.application.core.ValueMap;
import net.oopscraft.application.menu.Menu;
import net.oopscraft.application.menu.MenuService;


@PreAuthorize("hasAuthority('ADMIN_MENU')")
@Controller
@RequestMapping("/admin/menu")
public class MenuController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@Autowired
	MenuService menuService;
	
	@Autowired
	HttpServletResponse response;

	/**
	 * Forwards user page
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView menu() throws Exception {
		ModelAndView modelAndView = new ModelAndView("admin/menu.tiles");
		modelAndView.addObject("displayPolicies", Menu.DisplayPolicy.values());
		return modelAndView;
	}
	
	/**
	 * Gets menus
	 * 
	 * @param searchKey
	 * @param searchValue
	 * @param page
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getMenus", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional
	public String getMenus() throws Exception {
		List<Menu> menus = menuService.getMenus();
		return JsonUtils.toJson(menus);
	}
	
	/**
	 * Gets menu details.
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getMenu", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional
	public String getMenu(@RequestParam(value = "id") String id) throws Exception {
		Menu menu = menuService.getMenu(id);
		return JsonUtils.toJson(menu);
	}
	
	/**
	 * Gets bread crumbs
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getBreadCrumbs", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional
	public String getBreadCrumbs(@RequestParam(value = "id") String id) throws Exception {
		List<Menu> breadCrumbs = menuService.getBreadCrumbs(id);
		return JsonUtils.toJson(breadCrumbs);
	}

	/**
	 * Saves menu.
	 * 
	 * @param payload
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "saveMenu", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public String saveMenu(@RequestBody String payload) throws Exception {
		Menu role = JsonUtils.toObject(payload, Menu.class);
		role = menuService.saveMenu(role);
		return JsonUtils.toJson(role);
	}
	
	/**
	 * Removes menu.
	 * 
	 * @param payload
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "deleteMenu", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@Transactional(rollbackFor = Exception.class)
	public void deleteMenu(@RequestParam(value = "id") String id) throws Exception {
		menuService.deleteMenu(id);
	}

}
