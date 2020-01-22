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

import net.oopscraft.application.core.PageInfo;
import net.oopscraft.application.user.entity.Authority;
import net.oopscraft.application.user.entity.Role;

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
	public List<Authority> getAuthorities(final Authority authority, PageInfo pageInfo) throws Exception {
		Page<Authority> authorityPage = authorityRepository.findAll(new  Specification<Authority>() {
			@Override
			public Predicate toPredicate(Root<Authority> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				if(authority.getId() != null) {
					Predicate predicate = criteriaBuilder.and(criteriaBuilder.like(root.get("id").as(String.class), authority.getId() + '%'));
					predicates.add(predicate);
				}
				if(authority.getName() != null) {
					Predicate predicate = criteriaBuilder.and(criteriaBuilder.like(root.get("name").as(String.class), authority.getName() + '%'));
					predicates.add(predicate);
				}
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));	
			}
		}, pageInfo.toPageable());
		return authorityPage.getContent();
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
