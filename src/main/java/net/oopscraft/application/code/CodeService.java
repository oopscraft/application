package net.oopscraft.application.code;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import net.oopscraft.application.code.entity.Code;
import net.oopscraft.application.code.entity.CodeItem;
import net.oopscraft.application.core.PageInfo;

@Service
public class CodeService {
	
	@Autowired
	CodeRepository codeRepository;
	
	public enum CodeSearchType { ID,NAME	}
	
	/**
	 * Gets codes
	 * @param searchType
	 * @param searchValue
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<Code> getCodes(PageInfo pageInfo, CodeSearchType searchType, String searchValue) throws Exception {
		Pageable pageable = pageInfo.toPageable();
		Page<Code> codesPage = null;
		if(searchType == null) {
			codesPage = codeRepository.findAll(pageable);
		}else {
			switch(searchType) {
				case ID :
					codesPage = codeRepository.findByIdContaining(searchValue, pageable);
				break;
				case NAME :
					codesPage = codeRepository.findByNameContaining(searchValue, pageable);
				break;
			}
		}
		pageInfo.setTotalCount(codesPage.getTotalElements());
		return codesPage.getContent();
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
	public void saveCode(Code code) throws Exception {
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
		for (CodeItem item : code.getItems()) {
			item.setDisplaySeq(displaySeq ++);
			one.getItems().add(item);
		}
		codeRepository.saveAndFlush(one);
	}
	
	/**
	 * Deletes code
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public void deleteCode(String id) throws Exception {
		Code code = codeRepository.getOne(id);
		codeRepository.delete(code);
	}
}
