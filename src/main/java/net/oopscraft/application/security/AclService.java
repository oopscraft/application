package net.oopscraft.application.security;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import net.oopscraft.application.core.PageInfo;
import net.oopscraft.application.core.TextTable;
import net.oopscraft.application.security.repository.AclRepository;

@Service
public class AclService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AclService.class);
	
	@Autowired
	AclRepository aclRepository;
	
	public class SearchCondition {
		String uri;
		String name;
		public String getUri() {
			return uri;
		}
		public void setUri(String uri) {
			this.uri = uri;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	}

	/**
	 * Gets ACL List
	 * @param searchCondition
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<Acl> getAcls(SearchCondition searchCondition, PageInfo pageInfo) throws Exception {
		List<Acl> acls = null;
		Page<Acl> aclsPage = null;
		Pageable pageable = pageInfo.toPageable();
		if(!StringUtils.isEmpty(searchCondition.getUri())) {
			aclsPage = aclRepository.findByUriStartingWith(searchCondition.getName(), pageable);
		}else if(!StringUtils.isEmpty(searchCondition.getName())) {
			aclsPage = aclRepository.findByNameStartingWith(searchCondition.getName(), pageable);
		}else {
			aclsPage = aclRepository.findAllByOrderBySystemDataYnDescUriAsc(pageable);
		}
		if (pageInfo.isEnableTotalCount() == true) {
			pageInfo.setTotalCount(aclsPage.getTotalElements());
		}
		acls = aclsPage.getContent();
		LOGGER.info("+ authorities: {}", new TextTable(acls));
		return acls;
	}
	
}
