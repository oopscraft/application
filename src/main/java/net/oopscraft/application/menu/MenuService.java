package net.oopscraft.application.menu;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import net.oopscraft.application.core.PageInfo;
import net.oopscraft.application.menu.entity.Menu;


@Service
public class MenuService {

	@Autowired
	private MenuRepository menuRepository;

	/**
	 * Gets list of menu by search condition and value
	 * @param searchKey
	 * @param SearchValue
	 * @return
	 * @throws Exception
	 */
	public List<Menu> getMenus(final Menu menu, PageInfo pageInfo) throws Exception {
		Page<Menu> menusPage = menuRepository.findAll(new  Specification<Menu>() {
			@Override
			public Predicate toPredicate(Root<Menu> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				if(menu.getId() != null) {
					Predicate predicate = criteriaBuilder.and(criteriaBuilder.like(root.get("id").as(String.class), menu.getId() + '%'));
					predicates.add(predicate);
				}
				if(menu.getName() != null) {
					Predicate predicate = criteriaBuilder.and(criteriaBuilder.like(root.get("name").as(String.class), menu.getName() + '%'));
					predicates.add(predicate);
				}
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));	
			}
		}, pageInfo.toPageable());
		pageInfo.setTotalCount(menusPage.getTotalElements());
		return menusPage.getContent();
	}

	/**
	 * Gets detail of menu
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Menu getMenu(String id) throws Exception {
		return menuRepository.findOne(id);
	}
	
	/**
	 * Gets menu
	 * @param menu
	 * @return
	 * @throws Exception
	 */
	public Menu getMenu(Menu menu) throws Exception {
		return getMenu(menu.getId());
	}

	/**
	 * Saves menu details
	 * @param menu
	 * @throws Exception
	 */
	public Menu saveMenu(Menu menu) throws Exception {
		Menu one = menuRepository.findOne(menu.getId());
		if(one == null) {
			one = new Menu(menu.getId());
		}
		one.setUpperId(menu.getUpperId());
		one.setName(menu.getName());
		one.setIcon(menu.getIcon());
		one.setType(menu.getType());
		one.setValue(menu.getValue());
		one.setDescription(menu.getDescription());
		one.setDisplayPolicy(menu.getDisplayPolicy());
		one.setDisplayAuthorities(menu.getDisplayAuthorities());
		one.setAccessPolicy(menu.getAccessPolicy());
		one.setAccessAuthorities(menu.getAccessAuthorities());
		return menuRepository.save(one);
	}

	/**
	 * Removes menu details
	 * @param menu
	 * @throws Exception
	 */
	public void deleteMenu(Menu menu) throws Exception {
		menuRepository.delete(menu);
	}

}
