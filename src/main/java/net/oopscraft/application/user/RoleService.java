package net.oopscraft.application.user;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import net.oopscraft.application.common.PageInfo;
import net.oopscraft.application.user.entity.Authority;
import net.oopscraft.application.user.entity.Role;
import net.oopscraft.application.user.entity.User;

@Service
public class RoleService {
	
	RoleRepository roleRepository;
	
	/**
	 * Constructor
	 * @param roleRepository
	 */
	public RoleService(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}
	
	/**
	 * Returns roles
	 * @param user
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<Role> getRoles(final Role role, PageInfo pageInfo) throws Exception {
		Page<Role> rolesPage = roleRepository.findAll(new  Specification<Role>() {
			@Override
			public Predicate toPredicate(Root<Role> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				if(role.getId() != null) {
					Predicate predicate = criteriaBuilder.and(criteriaBuilder.like(root.get("id").as(String.class), role.getId() + '%'));
					predicates.add(predicate);
				}
				if(role.getName() != null) {
					Predicate predicate = criteriaBuilder.and(criteriaBuilder.like(root.get("name").as(String.class), role.getName() + '%'));
					predicates.add(predicate);
				}
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));	
			}
		}, pageInfo.toPageable());
		return rolesPage.getContent();
	}
	
	
	
	
	
	
	
	
	
	
	

//	@Autowired
//	RoleRepository roleRepository;

	public enum RoleSearchType { ID,NAME	}

	public List<Role> getRoles(PageInfo pageInfo, RoleSearchType searchType, String searchValue) throws Exception {
		Pageable pageable = pageInfo.toPageable();
		Page<Role> rolesPage = null;
		if(searchType == null) {
			rolesPage = roleRepository.findAll(pageable);
		}else {
			switch(searchType) {
				case ID :
					rolesPage = roleRepository.findByIdContaining(searchValue, pageable);
				break;
				case NAME :
					rolesPage = roleRepository.findByNameContaining(searchValue, pageable);
				break;
			}
		}
		if (pageInfo.isEnableTotalCount() == true) {
			pageInfo.setTotalCount(rolesPage.getTotalElements());
		}
		List<Role> roles = rolesPage.getContent();
		return roles;
	}

	/**
	 * Gets detail of role
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Role getRole(String id) throws Exception {
		Role role = roleRepository.findOne(id);
		return role;
	}

	/**
	 * Saves role
	 * 
	 * @param role
	 * @return
	 * @throws Exception
	 */
	public void saveRole(Role role) throws Exception {
		Role one = roleRepository.findOne(role.getId());
		if (one == null) {
			one = new Role();
			one.setId(role.getId());
			one.setAuthorities(new ArrayList<Authority>());
		}
		one.setName(role.getName());
		one.setDescription(role.getDescription());

		// add authorities
		one.getAuthorities().clear();
		for (Authority authority : role.getAuthorities()) {
			one.getAuthorities().add(authority);
		}

		roleRepository.save(one);
	}
	
	/**
	 * Deletes role
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public void deleteRole(String id) throws Exception {
		Role role = roleRepository.getOne(id);
		roleRepository.delete(role);
	}

}
