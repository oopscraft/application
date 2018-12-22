package net.oopscraft.application.code;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import net.oopscraft.application.code.repository.CodeRepository;
import net.oopscraft.application.core.PageInfo;
import net.oopscraft.application.core.TextTable;

@Service
public class CodeService {
	
	private static Logger LOGGER = LoggerFactory.getLogger(CodeService.class);
	
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
	 * Gets codes
	 * @param searchType
	 * @param searchValue
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<Code> getCodes(CodeSearch codeSearch, PageInfo pageInfo ) throws Exception {
		List<Code> codes = null;
		Page<Code> page = null;
		Pageable pageable = new PageRequest(pageInfo.getPage() - 1, pageInfo.getRows());
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
		LOGGER.debug("+ codes: {}", new TextTable(codes));
		return page.getContent();
	}
	
	/**
	 * Gets detail of code
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Code getCode(String id) throws Exception {
		Code Code = codeRepository.findOne(id);
		return Code;
	}
	
	/**
	 * Saves code
	 * 
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public Code saveCode(Code code) throws Exception {
		Code one = codeRepository.findOne(code.getId());
		if(one == null) {
			one = new Code();
			one.setId(code.getId());
		}
		one.setName(code.getName());
		one.setDescription(code.getDescription());
		
		// add items
		one.getItems().clear();
		int displaySeq = 0;
		for (Item item : code.getItems()) {
			item.setDisplaySeq(displaySeq ++);
			one.getItems().add(item);
		}
		
		codeRepository.save(one);
		return codeRepository.findOne(code.getId());
	}
	
	/**
	 * Removes code
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Code removeCode(String id) throws Exception {
		Code code = codeRepository.getOne(id);
		codeRepository.delete(code);
		return code;
	}
}
