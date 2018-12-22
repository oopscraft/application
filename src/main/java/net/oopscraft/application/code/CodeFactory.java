package net.oopscraft.application.code;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import net.oopscraft.application.code.repository.CodeRepository;
import net.oopscraft.application.core.PageInfo;
import net.oopscraft.application.core.TextTable;

@Component
public class CodeFactory {
	
	@Autowired
	CodeRepository codeRepository;
	
	/**
	 * Search condition class 
	 *
	 */
	public class CodeSearch {
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
	 * Gets Codes
	 * @param pageInfo
	 * @param codeSearch
	 * @return
	 * @throws Exception
	 */
	public List<Code> getCodes(PageInfo pageInfo, CodeSearch codeSearch) throws Exception {
		List<Code> codes = null;
		Page<Code> page = null;
		Pageable pageable = pageInfo.toPageable();
		if(!StringUtils.isEmpty(codeSearch.getId())) {
			page = codeRepository.findByIdStartingWith(codeSearch.getId(), pageable);
		}else if(!StringUtils.isEmpty(codeSearch.getId())) {
			page = codeRepository.findByNameStartingWith(codeSearch.getName(), pageable);
		}else {
			page = codeRepository.findAllByOrderBySystemDataYn(pageable);
		}
		codes = page.getContent();
		if (pageInfo.isEnableTotalCount() == true) {
			pageInfo.setTotalCount(page.getTotalElements());
		}
		return codes;
	}
	
	public Code getCode(String id) {
		Code code = codeRepository.findOne(id);
		return code;
	}

}
