package net.oopscraft.application.board;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import net.oopscraft.application.article.ArticleService;
import net.oopscraft.application.board.repository.BoardArticleRepository;
import net.oopscraft.application.core.PageInfo;

@Service
public class BoardArticleService extends ArticleService {
	
    @PersistenceContext
    EntityManager entityManager;

	@Autowired
	BoardArticleRepository boardArticleRepository;

	public enum ArticleSearchType {
		TITLE,
		TITLE_CONTENTS,
		USER
	}
	
	/**
	 * Gets articles
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<BoardArticle> getArticles(PageInfo pageInfo, String boardId, String categoryId, ArticleSearchType searchType, String searchValue) throws Exception {
		Pageable pageable = pageInfo.toPageable();
		Page<BoardArticle> articlesPage = null;
		if(categoryId == null || categoryId.trim().length() < 1) {
			if(searchType == null) {
				articlesPage = boardArticleRepository.findByBoardIdOrderByNoDesc(boardId, pageable);			
			}else {
				switch(searchType) {
					case TITLE :
						articlesPage = boardArticleRepository.findByBoardIdAndTitleContainingOrderByNoDesc(boardId, searchValue, pageable);
					break;
					case TITLE_CONTENTS :
						articlesPage = boardArticleRepository.findByBoardIdAndTitleContainingOrContentsContainingOrderByNoDesc(boardId, searchValue, searchValue, pageable);	
					break;
					case USER :
						articlesPage = boardArticleRepository.findByBoardIdAndUserIdContainingOrUserNicknameContainingOrderByNoDesc(boardId, searchValue, searchValue, pageable);	
					break;
				}
			}
		}else {
			if(searchType == null) {
				articlesPage = boardArticleRepository.findByBoardIdAndCategoryIdOrderByNoDesc(boardId, categoryId, pageable);			
			}else {
				switch(searchType) {
					case TITLE :
						articlesPage = boardArticleRepository.findByBoardIdAndCategoryIdAndTitleContainingOrderByNoDesc(boardId, categoryId, searchValue, pageable);
					break;
					case TITLE_CONTENTS :
						articlesPage = boardArticleRepository.findByBoardIdAndCategoryIdAndTitleContainingOrContentsContainingOrderByNoDesc(boardId, categoryId, searchValue, searchValue, pageable);	
					break;
					case USER :
						articlesPage = boardArticleRepository.findByBoardIdAndCategoryIdAndUserIdContainingOrUserNicknameContainingOrderByNoDesc(boardId, categoryId, searchValue, searchValue, pageable);	
					break;
				}
			}
		}
		if(pageInfo.isEnableTotalCount()) {
			pageInfo.setTotalCount(articlesPage.getTotalElements());
		}
		List<BoardArticle> articles = articlesPage.getContent();
		return articles;
	}
	
	/**
	 * Gets board article
	 * @param boardId
	 * @param no
	 * @return
	 * @throws Exception
	 */
	public BoardArticle getArticle(long no) throws Exception {
		BoardArticle boardArticle = boardArticleRepository.findOne(no);
		boardArticle.setEntityManager(entityManager);
		return boardArticle;
	}
	
	/**
	 * Saves board article.
	 * @param boardId
	 * @param article
	 * @throws Exception
	 */
	public void saveArticle(BoardArticle article) throws Exception {
		super.saveArticle(article);
	}

	/**
	 * Deletes board article
	 * @param no
	 * @throws Exception
	 */
	public void deleteArticle(BoardArticle article) throws Exception {
		super.deleteArticle(article);
	}
}
