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
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import net.oopscraft.application.core.IdGenerator;
import net.oopscraft.application.core.Pagination;
import net.oopscraft.application.core.jpa.SystemEmbeddedException;


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
	public List<Menu> getMenus(final Menu menu, Pagination pagination) throws Exception {
		List<Order> orders = new ArrayList<Order>();
		orders.add(new Order(Direction.ASC, "upperId"));
		orders.add(new Order(Direction.ASC, "displayNo"));
		PageRequest pageRequest = pagination.toPageRequest(new Sort(orders));
		Page<Menu> menusPage = menuRepository.findAll(new Specification<Menu>() {
			@Override
			public Predicate toPredicate(Root<Menu> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				if(menu.getId() != null) {
					Predicate predicate = criteriaBuilder.and(criteriaBuilder.like(root.get("id").as(String.class), '%' + menu.getId() + '%'));
					predicates.add(predicate);
				}
				if(menu.getName() != null) {
					Predicate predicate = criteriaBuilder.and(criteriaBuilder.like(root.get("name").as(String.class), '%' + menu.getName() + '%'));
					predicates.add(predicate);
				}
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));	
			}
		}, pageRequest);
		pagination.setTotalCount(menusPage.getTotalElements());
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
		Menu one = null;
		if(menu.getId() == null || (one = menuRepository.findOne(menu.getId())) == null) {
			one = new Menu(IdGenerator.uuid());
		}
		one.setUpperId(menu.getUpperId());
		one.setName(menu.getName());
		one.setIcon(menu.getIcon());
		one.setDescription(menu.getDescription());
		one.setLinkUrl(menu.getLinkUrl());
		one.setLinkTarget(menu.getLinkTarget());
		one.setDisplayPolicy(menu.getDisplayPolicy());
		one.setDisplayAuthorities(menu.getDisplayAuthorities());
		return menuRepository.save(one);
	}

	/**
	 * Removes menu details
	 * @param menu
	 * @throws Exception
	 */
	public void deleteMenu(Menu menu) throws Exception {
		Menu one = menuRepository.findOne(menu.getId());
		if(one.isSystemEmbedded()) {
			throw new SystemEmbeddedException();
		}
		menuRepository.delete(one);
	}

	/**
	 * Changes group upper id
	 * @param id
	 * @param upperId
	 * @return
	 */
	public Menu changeUpperId(String id, String upperId) {
		Menu one = menuRepository.findOne(id);
		one.setUpperId(upperId);
		return menuRepository.save(one);
	}

}
