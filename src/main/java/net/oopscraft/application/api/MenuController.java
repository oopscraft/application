package net.oopscraft.application.api;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import net.oopscraft.application.core.PageInfo;
import net.oopscraft.application.menu.MenuService;
import net.oopscraft.application.menu.entity.Menu;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

	@Autowired
	HttpServletResponse response;
	
	@Autowired
	MenuService menuService;
	
	/**
	 * Returns menus
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<Menu> getMenus(@ModelAttribute Menu menu, @ModelAttribute PageInfo pageInfo) throws Exception {
		pageInfo.setEnableTotalCount(true);
		List<Menu> menus = menuService.getMenus(menu, pageInfo);
		response.setHeader(HttpHeaders.CONTENT_RANGE, pageInfo.getContentRange());
		return menus;
	}
	
	/**
	 * Returns menu
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Menu getMenu(@ModelAttribute Menu menu) throws Exception {
		return menuService.getMenu(menu);
	}
	
}
