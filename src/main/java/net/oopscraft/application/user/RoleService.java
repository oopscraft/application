package net.oopscraft.application.user;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import net.oopscraft.application.core.Pagination;
import net.oopscraft.application.core.jpa.SystemEmbeddedException;

@Service
public class RoleService {
	
	@Autowired
	RoleRepository roleRepository;
	
	/**
	 * Returns roles
	 * @param user
	 * @param pagination
	 * @return
	 * @throws Exception
	 */
	public List<Role> getRoles(final Role role, Pagination pagination) throws Exception {
		Page<Role> rolesPage = roleRepository.findAll(new  Specification<Role>() {
			@Override
			public Predicate toPredicate(Root<Role> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				if(role.getId() != null) {
					Predicate predicate = criteriaBuilder.and(criteriaBuilder.like(root.get("id").as(String.class), '%' + role.getId() + '%'));
					predicates.add(predicate);
				}
				if(role.getName() != null) {
					Predicate predicate = criteriaBuilder.and(criteriaBuilder.like(root.get("name").as(String.class), '%' + role.getName() + '%'));
					predicates.add(predicate);
				}
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));	
			}
		}, pagination.toPageRequest());
		pagination.setTotalCount(rolesPage.getTotalElements());
		return rolesPage.getContent();
	}
	
	/**
	 * Gets role
	 * @param role
	 * @return
	 * @throws Exception
	 */
	public Role getRole(String id) throws Exception {
		return roleRepository.findOne(id);
	}
	
	/**
	 * Saves role
	 * @param role
	 * @return
	 * @throws Exception
	 */
	public Role saveRole(Role role) throws Exception {
		Role one = roleRepository.findOne(role.getId());
		if(one == null) {
			one = new Role(role.getId());
		}
		one.setName(role.getName());
		one.setIcon(role.getIcon());
		one.setDescription(role.getDescription());
		one.setAuthorities(role.getAuthorities());
		return roleRepository.save(one);
	}
	
	/**
	 * Deletes role
	 * @param role
	 * @throws Exception
	 */
	public void deleteRole(Role role) throws Exception {
		Role one = roleRepository.findOne(role.getId());
		if(one.isSystemEmbedded()) {
			throw new SystemEmbeddedException();
		}
		roleRepository.delete(one);
	}

}
