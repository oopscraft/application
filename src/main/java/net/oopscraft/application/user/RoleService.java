package net.oopscraft.application.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import net.oopscraft.application.core.PageInfo;
import net.oopscraft.application.user.repository.RoleRepository;

@Service
public class RoleService {

	@Autowired
	RoleRepository roleRepository;

	public enum RoleSearchType { ID,NAME	}

	public List<Role> getRoles(PageInfo pageInfo, RoleSearchType searchType, String searchValue) throws Exception {
		Pageable pageable = pageInfo.toPageable();
		Page<Role> rolesPage = null;
		if(searchType == null) {
			rolesPage = roleRepository.findAll(pageable);
		}else {
			switch(searchType) {
				case ID :
					rolesPage = roleRepository.findByIdStartingWith(searchValue, pageable);
				break;
				case NAME :
					rolesPage = roleRepository.findByNameStartingWith(searchValue, pageable);
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
