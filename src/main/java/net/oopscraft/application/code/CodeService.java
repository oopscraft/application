package net.oopscraft.application.code;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import net.oopscraft.application.code.entity.Code;
import net.oopscraft.application.code.entity.CodeItem;
import net.oopscraft.application.core.PageInfo;

@Service
public class CodeService {
	
	@Autowired
	CodeRepository codeRepository;
	
	/**
	 * Returns codes
	 * @param code
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<Code> getCodes(final Code code, PageInfo pageInfo) throws Exception {
		Page<Code> codesPage = codeRepository.findAll(new  Specification<Code>() {
			@Override
			public Predicate toPredicate(Root<Code> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				if(code.getId() != null) {
					Predicate predicate = criteriaBuilder.and(criteriaBuilder.like(root.get("id").as(String.class), code.getId() + '%'));
					predicates.add(predicate);
				}
				if(code.getName() != null) {
					Predicate predicate = criteriaBuilder.and(criteriaBuilder.like(root.get("name").as(String.class), code.getName() + '%'));
					predicates.add(predicate);
				}
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));	
			}
		}, pageInfo.toPageable());
		pageInfo.setTotalCount(codesPage.getTotalElements());
		return codesPage.getContent();
	}
	
	/**
	 * Gets code
	 * @param id
	 * @return
	 */
	public Code getCode(String id) {
		return codeRepository.findOne(id);
	}
	
	/**
	 * Gets code
	 * @param articleId
	 * @return
	 * @throws Exception
	 */
	public Code getCode(Code code) throws Exception {
		return getCode(code.getId());
	}
	
	/**
	 * Saves code
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public Code saveCode(Code code) throws Exception {
		Code one = codeRepository.findOne(code.getId());
		if(one == null) {
			one = new Code(code.getId());
		}
		one.setName(code.getName());
		one.setDescription(code.getDescription());
		
		// resets code items
		one.getItems().clear();
		int sequence = 1;
		for (CodeItem item : code.getItems()) {
			item.setSequence(sequence ++);
			one.getItems().add(item);
		}
		
		// save code entity
		return codeRepository.save(one);
	}
	
	/**
	 * Deletes code
	 * @param code
	 * @throws Exception
	 */
	public void deleteCode(Code code) throws Exception {
		codeRepository.delete(code);
	}

}
