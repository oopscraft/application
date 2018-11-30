package net.oopscraft.application.user;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import net.oopscraft.application.core.PageInfo;
import net.oopscraft.application.core.TextTable;
import net.oopscraft.application.user.repository.RoleRepository;

@Service
public class RoleService {

	private static Logger LOGGER = LoggerFactory.getLogger(RoleService.class);

	@Autowired
	RoleRepository roleRepository;

	/**
	 * Search condition class
	 *
	 */
	public class SearchCondition {
		String id;
		String name;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	public List<Role> getRoles(SearchCondition searchCondition, PageInfo pageInfo) throws Exception {
		List<Role> roles = null;
		Page<Role> page = null;
		Pageable pageable = new PageRequest(pageInfo.getPage() - 1, pageInfo.getRows());
		if (!StringUtils.isEmpty(searchCondition.getId())) {
			page = roleRepository.findByIdStartingWith(searchCondition.getId(), pageable);
		} else if (!StringUtils.isEmpty(searchCondition.getId())) {
			page = roleRepository.findByNameStartingWith(searchCondition.getName(), pageable);
		} else {
			page = roleRepository.findAll(pageable);
		}
		roles = page.getContent();
		if (pageInfo.isEnableTotalCount() == true) {
			pageInfo.setTotalCount(page.getTotalElements());
		}
		LOGGER.info(new TextTable(roles).toString());
		LOGGER.info("+ roles: {}", new TextTable(roles));
		return page.getContent();
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
	public Role saveRole(Role role) throws Exception {
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
		return roleRepository.findOne(role.getId());
	}
	
	/**
	 * Removes role
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Role removeRole(String id) throws Exception {
		Role role = roleRepository.getOne(id);
		roleRepository.delete(role);
		return role;
	}

}
