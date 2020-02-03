package net.oopscraft.application.board;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import net.oopscraft.application.board.entity.BoardArticle;
import net.oopscraft.application.core.IdGenerator;
import net.oopscraft.application.core.PageInfo;

@Service
public class BoardArticleService {
	
	@Autowired
	BoardArticleRepository boardArticleRepository;
	
	/**
	 * Returns board articles
	 * @param code
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<BoardArticle> getBoardArticles(final BoardArticle boardArticle, PageInfo pageInfo) throws Exception {
		Page<BoardArticle> boardArticlesPage = boardArticleRepository.findAll(new  Specification<BoardArticle>() {
			@Override
			public Predicate toPredicate(Root<BoardArticle> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				
				// boardId
				predicates.add(
					criteriaBuilder.and(criteriaBuilder.equal(root.get("boardId").as(String.class), boardArticle.getBoardId()))
				);
				
				// categoryId
				if(boardArticle.getCategoryId() != null) {
					predicates.add(
						criteriaBuilder.and(criteriaBuilder.equal(root.get("categoryId").as(String.class), boardArticle.getCategoryId()))
					);
				}

				// title
				if(boardArticle.getTitle() != null) {
					predicates.add(
						criteriaBuilder.and(criteriaBuilder.like(root.get("title").as(String.class), '%' + boardArticle.getTitle() + '%'))
					);
				}
				
				// contents
				if(boardArticle.getContents() != null) {
					predicates.add(
						criteriaBuilder.and(criteriaBuilder.like(root.get("contents").as(String.class), '%' + boardArticle.getContents() + '%'))
					);
				}

				// returns 
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));	
			}
		}, pageInfo.toPageable());
		pageInfo.setTotalCount(boardArticlesPage.getTotalElements());
		return boardArticlesPage.getContent();
	}
	
	/**
	 * Gets board article
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public BoardArticle getBoardArticle(BoardArticle boardArticle) throws Exception {
		return boardArticleRepository.findOne(boardArticle.getId());
	}
	
	/**
	 * Saves code
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public BoardArticle saveBoardArticle(BoardArticle boardArticle) throws Exception {
		if(StringUtils.isBlank(boardArticle.getId())) {
			boardArticle.setId(IdGenerator.uuid());
		}
		BoardArticle one = boardArticleRepository.findOne(boardArticle.getId());
		if(one == null) {
			one = new BoardArticle(boardArticle.getBoardId(),boardArticle.getId());
		}
		one.setTitle(boardArticle.getTitle());
		one.setContents(boardArticle.getContents());
		
		// save code entity
		return boardArticleRepository.save(one);
	}
	
	/**
	 * Deletes board article
	 * @param code
	 * @throws Exception
	 */
	public void deleteBoardArticle(BoardArticle boardArticle) throws Exception {
		boardArticleRepository.delete(boardArticle);
	}

}
