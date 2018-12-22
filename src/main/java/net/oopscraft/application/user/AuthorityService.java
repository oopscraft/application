package net.oopscraft.application.user;

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
import net.oopscraft.application.user.repository.AuthorityRepository;

@Service
public class AuthorityService {
	
	private static Logger LOGGER = LoggerFactory.getLogger(AuthorityService.class);
	
	@Autowired
	AuthorityRepository authorityRepository;
	
	/**
	 * Search condition class 
	 *
	 */
	public class AuthoritySearch {
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
	
	/**
	 * Gets authorities
	 * @param searchType
	 * @param searchValue
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<Authority> getAuthorities(AuthoritySearch authoritySearch, PageInfo pageInfo ) throws Exception {
		List<Authority> authorities = null;
		Page<Authority> page = null;
		Pageable pageable = new PageRequest(pageInfo.getPage() - 1, pageInfo.getRows());
		if(!StringUtils.isEmpty(authoritySearch.getId())) {
			page = authorityRepository.findByIdStartingWith(authoritySearch.getId(), pageable);
		}else if(!StringUtils.isEmpty(authoritySearch.getId())) {
			page = authorityRepository.findByNameStartingWith(authoritySearch.getName(), pageable);
		}else {
			page = authorityRepository.findAllByOrderBySystemDataYnDescSystemInsertDateDesc(pageable);
		}
		authorities = page.getContent();
		if (pageInfo.isEnableTotalCount() == true) {
			pageInfo.setTotalCount(page.getTotalElements());
		}
		LOGGER.info(new TextTable(authorities).toString());
		LOGGER.info("+ authorities: {}", new TextTable(authorities));
		return page.getContent();
	}
	
	/**
	 * Gets detail of authority
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Authority getAuthority(String id) throws Exception {
		Authority authority = authorityRepository.findOne(id);
		return authority;
	}
	
	/**
	 * Saves authority
	 * 
	 * @param authority
	 * @return
	 * @throws Exception
	 */
	public Authority saveAuthority(Authority authority) throws Exception {
		Authority one = authorityRepository.findOne(authority.getId());
		if(one == null) {
			one = new Authority();
			one.setId(authority.getId());
		}
		one.setName(authority.getName());
		one.setDescription(authority.getDescription());
		authorityRepository.save(one);
		return authorityRepository.findOne(authority.getId());
	}
	
	/**
	 * Removes authority
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Authority removeAuthority(String id) throws Exception {
		Authority authority = authorityRepository.getOne(id);
		authorityRepository.delete(authority);
		return authority;
	}
	
}
