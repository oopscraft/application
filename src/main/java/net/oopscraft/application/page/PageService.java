package net.oopscraft.application.page;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import net.oopscraft.application.board.Board;
import net.oopscraft.application.core.PageInfo;
import net.oopscraft.application.page.repository.PageRepository;

@Service
public class PageService {
	
	@Autowired
	PageRepository pageRepository;
	
	public enum PageSearchType { ID,NAME	}
	
	/**
	 * Gets pages
	 * @param pageInfo
	 * @param searchType
	 * @param searchValue
	 * @return
	 * @throws Exception
	 */
	public List<Page> getPages(PageInfo pageInfo, PageSearchType searchType, String searchValue) throws Exception {
		Pageable pageable = pageInfo.toPageable();
		org.springframework.data.domain.Page<Page> pagesPage = null;
		if(searchType == null) {
			pagesPage = pageRepository.findAll(pageable);
		}
		pageInfo.setTotalCount(pagesPage.getTotalElements());
		return pagesPage.getContent();
	}
	
	/**
	 * Gets page
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Page getPage(String id) throws Exception {
		Page page = pageRepository.findOne(id);
		return page;
	}
	
	/**
	 * Saves page
	 * @param page
	 * @throws Exception
	 */
	public void savePage(Page page) throws Exception {
		Page one = pageRepository.findOne(page.getId());
		if(one == null) {
			pageRepository.saveAndFlush(page);
		}else {
			one.setName(page.getName());
			one.setType(page.getType());
			one.setValue(page.getValue());
			one.setLayoutId(page.getLayoutId());
			one.setAccessPolicy(page.getAccessPolicy());
			one.setAccessAuthorities(page.getAccessAuthorities());
			pageRepository.saveAndFlush(one);
		}
	}

	/**
	 * Deletes page
	 * @param id
	 * @throws Exception
	 */
	public void deletePage(String id) throws Exception {
		Page page = pageRepository.findOne(id);
		pageRepository.delete(page);
	}

}
