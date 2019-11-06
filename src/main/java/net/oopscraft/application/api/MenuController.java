package net.oopscraft.application.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import net.oopscraft.application.core.JsonUtility;
import net.oopscraft.application.menu.MenuService;
import net.oopscraft.application.menu.entity.Menu;

@Controller
@RequestMapping("/api")
public class MenuController {

	@Autowired
	MenuService menuService;
	
	/**
	 * Gets menus
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/menus", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> getMenus() throws Exception {
		List<Menu> menus = menuService.getMenus();
		return new ResponseEntity<>(JsonUtility.toJson(menus), HttpStatus.OK);
	}
	
	/**
	 * Gets specified menu
	 * @param menuId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/menu/{menuId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> getMenu(@RequestParam(value = "menuId") String menuId) throws Exception {
		Menu menu = menuService.getMenu(menuId);
		return new ResponseEntity<>(JsonUtility.toJson(menu), HttpStatus.OK);
	}

}
