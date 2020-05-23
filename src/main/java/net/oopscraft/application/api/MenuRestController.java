package net.oopscraft.application.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;

import net.oopscraft.application.core.Pagination;
import net.oopscraft.application.menu.Menu;
import net.oopscraft.application.menu.MenuService;
import net.oopscraft.application.security.SecurityEvaluator;

@CrossOrigin
@RestController
@RequestMapping("/api/menus")
public class MenuRestController {
	
	@Autowired
	MenuService menuService;
	
	@Autowired
	LocaleResolver localeResolver;
	
	/**
	 * Returns menus
	 * @param request
	 * @param response
	 * @param userDetails
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<Menu> getMenus(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Menu menu = new Menu();
		Pagination pagination = new Pagination();
		List<Menu> menus = menuService.getMenus(menu, pagination);
		
		// filter available menus
		List<Menu> availableMenus = getAvailableMenus(menus);
		
		// adjusts user language
		Locale locale = localeResolver.resolveLocale(request);
		translateMenus(availableMenus, locale);
		
		response.setHeader(HttpHeaders.CONTENT_RANGE, pagination.getContentRange());
		return availableMenus;
	}
	
	/**
	 * Returns menu
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Menu getMenu(@PathVariable("id") String id, HttpServletRequest request) throws Exception {
		Menu menu = menuService.getMenu(new Menu(id));
		if(menu == null) {
			return null;
		}

		// checks available menu
		if(isAvailableMenu(menu) == false) {
			return null;
		}
		
		// translate menu
		Locale locale = localeResolver.resolveLocale(request);
		translateMenu(menu, locale);
		
		// returns
		return menu;
	}
	
	
	/**
	 * getAvailableMenus
	 * @param menus
	 * @param userDetails
	 * @return
	 * @throws Exception
	 */
	private List<Menu> getAvailableMenus(List<Menu> menus) throws Exception {
		List<Menu> availableMenus = new ArrayList<Menu>();
		for(Menu menu : menus) {
			if(isAvailableMenu(menu)){
				availableMenus.add(menu);
			}
		}
		return availableMenus;
	}
	
	/**
	 * isAvailableMenu
	 * @param menu
	 * @param userDetails
	 * @return
	 * @throws Exception
	 */
	private boolean isAvailableMenu(Menu menu) throws Exception {
		return SecurityEvaluator.hasPolicyAuthority(menu.getDisplayPolicy(), menu.getDisplayAuthorities());
	}
	
	/**
	 * translateMenu
	 * @param menu
	 * @param locale
	 * @throws Exception
	 */
	private void translateMenu(Menu menu, Locale locale) throws Exception {
		if(locale != null) {
			menu.setName(menu.getName(locale.getLanguage()));
		}
	}
	
	/**
	 * translateMenus
	 * @param menus
	 * @param locale
	 * @throws Exception
	 */
	private void translateMenus(List<Menu> menus, Locale locale) throws Exception {
		if(locale != null) {
			for(Menu menu : menus) {
				translateMenu(menu, locale);
			}
		}
	}
	
}
