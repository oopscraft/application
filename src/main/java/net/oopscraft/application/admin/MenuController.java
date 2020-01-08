package net.oopscraft.application.admin;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

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

import net.oopscraft.application.core.JsonConverter;
import net.oopscraft.application.menu.MenuService;
import net.oopscraft.application.menu.entity.Menu;


@PreAuthorize("hasAuthority('ADMIN_MENU')")
@Controller
@RequestMapping("/admin/menu")
public class MenuController {

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
		modelAndView.addObject("types", Menu.Type.values());
		modelAndView.addObject("policies", Menu.Policy.values());
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
		return JsonConverter.toJson(menus);
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
		return JsonConverter.toJson(menu);
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
		return JsonConverter.toJson(breadCrumbs);
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
		Menu role = JsonConverter.toObject(payload, Menu.class);
		role = menuService.saveMenu(role);
		return JsonConverter.toJson(role);
	}
	
	/**
	 * Deletes menu.
	 * 
	 * @param payload
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "deleteMenu", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public String deleteMenu(@RequestParam(value = "id") String id) throws Exception {
		menuService.deleteMenu(id);
		return JsonConverter.toJson(id);
	}

}
