package net.oopscraft.application.api;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import net.oopscraft.application.core.Pagination;
import net.oopscraft.application.menu.MenuService;
import net.oopscraft.application.menu.entity.Menu;

@CrossOrigin
@RestController
@RequestMapping("/api/menus")
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
	public List<Menu> getMenus() throws Exception {
		Menu menu = new Menu();
		Pagination pagination = new Pagination();
		List<Menu> menus = menuService.getMenus(menu, pagination);
		response.setHeader(HttpHeaders.CONTENT_RANGE, pagination.getContentRange());
		return menus;
	}
	
	/**
	 * Returns menu
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Menu getMenu(@PathVariable("id") String id) throws Exception {
		return menuService.getMenu(new Menu(id));
	}
	
}
