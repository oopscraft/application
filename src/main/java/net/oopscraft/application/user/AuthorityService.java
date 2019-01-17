package net.oopscraft.application.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import net.oopscraft.application.core.PageInfo;
import net.oopscraft.application.user.repository.AuthorityRepository;

@Service
public class AuthorityService {
	
	@Autowired
	AuthorityRepository authorityRepository;
	
	public enum AuthoritySearchType { ID,NAME	}
	
	/**
	 * Gets authorities
	 * @param searchType
	 * @param searchValue
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<Authority> getAuthorities(PageInfo pageInfo, AuthoritySearchType searchType, String searchValue) throws Exception {
		Pageable pageable = pageInfo.toPageable();
		Page<Authority> authoritiesPage = null;
		if(searchType == null) {
			authoritiesPage = authorityRepository.findAll(pageable);
		}else {
			switch(searchType) {
				case ID :
					authoritiesPage = authorityRepository.findByIdStartingWith(searchValue, pageable);
				break;
				case NAME :
					authoritiesPage = authorityRepository.findByNameStartingWith(searchValue, pageable);
				break;
			}
		}
		if (pageInfo.isEnableTotalCount() == true) {
			pageInfo.setTotalCount(authoritiesPage.getTotalElements());
		}
		List<Authority> authorities = authoritiesPage.getContent();
		return authorities;
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
	public void saveAuthority(Authority authority) throws Exception {
		Authority one = authorityRepository.findOne(authority.getId());
		if(one == null) {
			one = new Authority();
			one.setId(authority.getId());
		}
		one.setName(authority.getName());
		one.setDescription(authority.getDescription());
		authorityRepository.save(one);
	}
	
	/**
	 * Deletes authority
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public void deleteAuthority(String id) throws Exception {
		Authority authority = authorityRepository.getOne(id);
		authorityRepository.delete(authority);
	}
	
}
