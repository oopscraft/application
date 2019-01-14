package net.oopscraft.application.menu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.oopscraft.application.core.RandomUtils;
import net.oopscraft.application.core.StringUtils;
import net.oopscraft.application.core.TextTable;
import net.oopscraft.application.menu.repository.MenuRepository;


@Service
public class MenuService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MenuService.class);

	@Autowired
	private MenuRepository menuRepository;

	/**
	 * Gets list of menu by search condition and value
	 * 
	 * @param searchKey
	 * @param SearchValue
	 * @return
	 * @throws Exception
	 */
	public List<Menu> getMenus() throws Exception {
		List<Menu> menus = menuRepository.findByUpperIdIsNullOrderByDisplaySeqAsc();
		for (Menu menu : menus) {
			fillChildMenuRecursively(menu);
		}
		LOGGER.debug("menus {}", new TextTable(menus));
		return menus;
	}

	/**
	 * Gets detail of menu
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Menu getMenu(String id) throws Exception {
		Menu menu = menuRepository.findOne(id);
		if (menu != null) {
			fillChildMenuRecursively(menu);
		}
		return menu;
	}

	/**
	 * Fills child menu recursively
	 * 
	 * @param menu
	 * @throws Exception
	 */
	private void fillChildMenuRecursively(Menu menu) throws Exception {
		List<Menu> childMenus = menuRepository.findByUpperIdOrderByDisplaySeqAsc(menu.getId());
		menu.setChildMenus(childMenus);
		for (Menu childMenu : childMenus) {
			fillChildMenuRecursively(childMenu);
		}
	}

	/**
	 * Gets bread crumbs
	 * 
	 * @param id
	 * @return
	 */
	public List<Menu> getBreadCrumbs(String id) {
		List<Menu> breadCrumbs = new ArrayList<Menu>();
		while (id != null) {
			Menu menu = menuRepository.findOne(id);
			if (menu != null) {
				breadCrumbs.add(menu);
				id = menu.getUpperId();
				continue;
			}
			break;
		}
		Collections.reverse(breadCrumbs);
		return breadCrumbs;
	}

	/**
	 * Saves menu details
	 * 
	 * @param menu
	 * @throws Exception
	 */
	public Menu saveMenu(Menu menu) throws Exception {
		
		Menu one = null;
		if(StringUtils.isEmpty(menu.getId()) == true) {
			one = new Menu();
			one.setId(RandomUtils.generateID());
		}else if(menuRepository.exists(menu.getId()) == false) {
			one = new Menu();
			one.setId(menu.getId());
		}else {
			one = menuRepository.findOne(menu.getId());
		}

		// sets properties
		one.setUpperId(menu.getUpperId());
		one.setName(menu.getName());
		one.setIcon(menu.getIcon());
		one.setLink(menu.getLink());
		one.setDescription(menu.getDescription());
		one.setDisplaySeq(menu.getDisplaySeq());
		one.setDisplayPolicy(menu.getDisplayPolicy());
		
		// Checks id and upperId is same.
		if (one.getId().equals(one.getUpperId()) == true) {
			throw new Exception("Menu.id and Menu.upperId is same.(infinit loop)");
		}

		// adds authorities
		one.setDisplayAuthorities(menu.getDisplayAuthorities());

		return menuRepository.save(one);
	}

	/**
	 * Removes menu details
	 * 
	 * @param id
	 * @throws Exception
	 */
	public void deleteMenu(String id) throws Exception {
		Menu menu = menuRepository.findOne(id);

		// checks child menus
		List<Menu> childMenus = menuRepository.findByUpperIdOrderByDisplaySeqAsc(id);
		if (childMenus.size() > 0) {
			throw new Exception("Can not remove.because has child menus.");
		}

		// deletes menu
		menuRepository.delete(menu);
	}

}
