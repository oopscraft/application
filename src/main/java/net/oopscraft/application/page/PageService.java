package net.oopscraft.application.page;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import net.oopscraft.application.core.IdGenerator;
import net.oopscraft.application.core.Pagination;
import net.oopscraft.application.core.jpa.SystemEmbeddedException;
import net.oopscraft.application.message.Message;
import net.oopscraft.application.message.MessageI18n;

@Service
public class PageService {
	
	@Autowired
	PageRepository pageRepository;
	
	/**
	 * Gets list of page by search condition and value
	 * @param searchKey
	 * @param SearchValue
	 * @return
	 * @throws Exception
	 */
	public List<Page> getPages(final Page page, Pagination pagination) throws Exception {
		org.springframework.data.domain.Page<Page> pagePage = pageRepository.findAll(new Specification<Page>() {
			@Override
			public Predicate toPredicate(Root<Page> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				if(page.getName() != null) {
					Predicate predicate = criteriaBuilder.and(criteriaBuilder.like(root.get("name").as(String.class), '%' + page.getName() + '%'));
					predicates.add(predicate);
				}
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));	
			}
		}, pagination.toPageRequest());
		pagination.setTotalCount(pagePage.getTotalElements());
		return pagePage.getContent();
	}

	/**
	 * Gets detail of page
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Page getPage(String id) throws Exception {
		return pageRepository.findOne(id);
	}

	/**
	 * Saves page details
	 * @param page
	 * @throws Exception
	 */
	public Page savePage(Page page) throws Exception {
		Page one = null;
		if(page.getId() == null || (one = pageRepository.findOne(page.getId())) == null) {
			one = new Page(IdGenerator.uuid());
		}
		one.setName(page.getName());
		one.setDescription(page.getDescription());
		one.setFormat(page.getFormat());
		one.setContents(page.getContents());
		
		// read policy
		one.setReadPolicy(page.getReadPolicy());
		one.getReadAuthorities().clear();
		one.getReadAuthorities().addAll(page.getReadAuthorities());
		
		// edit policy
		one.setEditPolicy(page.getEditPolicy());
		one.getEditAuthorities().clear();
		one.getEditAuthorities().addAll(page.getEditAuthorities());
		
		// returns
		return pageRepository.save(one);
	}

	/**
	 * Removes page details
	 * @param page
	 * @throws Exception
	 */
	public void deletePage(Page page) throws Exception {
		Page one = pageRepository.findOne(page.getId());
		if(one.isSystemEmbedded()) {
			throw new SystemEmbeddedException();
		}
		pageRepository.delete(one);
	}
	
	/**
	 * Returns message i18ns
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public List<PageI18n> getPageI18ns(String id) throws Exception {
		Page one = pageRepository.findOne(id);
		return one.getI18ns();
	}
	
	/**
	 * saveMessageI18ns
	 * @param id
	 * @param messageI18ns
	 * @return
	 * @throws Exception
	 */
	public List<PageI18n> savePageI18ns(String id, List<PageI18n> pageI18ns) throws Exception {
		Page one = pageRepository.findOne(id);
		one.getI18ns().clear();
		for(PageI18n element : pageI18ns) {
			element.setId(id);
			one.getI18ns().add(element);
		}
		one = pageRepository.save(one);
		return one.getI18ns();
	}

}
