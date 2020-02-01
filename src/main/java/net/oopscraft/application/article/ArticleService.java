package net.oopscraft.application.article;

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

import net.oopscraft.application.article.entity.Article;
import net.oopscraft.application.core.IdGenerator;
import net.oopscraft.application.core.PageInfo;

@Service
public class ArticleService {

	@Autowired
	ArticleRepository articleRepository;
	
	/**
	 * Returns articles
	 * @param article
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<Article> getArticles(final Article article, PageInfo pageInfo) throws Exception {
		Page<Article> propertiesPage = articleRepository.findAll(new  Specification<Article>() {
			@Override
			public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				if(article.getTitle() != null) {
					Predicate predicate = criteriaBuilder.and(criteriaBuilder.like(root.get("name").as(String.class), '%' + article.getTitle() + '%'));
					predicates.add(predicate);
				}
				if(article.getContents() != null) {
					Predicate predicate = criteriaBuilder.and(criteriaBuilder.like(root.get("contents").as(String.class), '%' + article.getContents() + '%'));
					predicates.add(predicate);
				}
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));	
			}
		}, pageInfo.toPageable());
		pageInfo.setTotalCount(propertiesPage.getTotalElements());
		return propertiesPage.getContent();
	}
	
	/**
	 * Gets article
	 * @param article
	 * @return
	 * @throws Exception
	 */
	public Article getArticle(String id) throws Exception {
		return articleRepository.findOne(id);
	}
	
	/**
	 * Saves article
	 * @param article
	 * @return
	 * @throws Exception
	 */
	public Article saveArticle(Article article) throws Exception {
		if(StringUtils.isBlank(article.getId())) {
			article.setId(IdGenerator.uuid());
		}
		Article one = articleRepository.findOne(article.getId());
		if(one == null) {
			one = new Article(IdGenerator.uuid());
		}
		one.setTitle(article.getTitle());
		one.setContents(article.getContents());
		return articleRepository.save(one);
	}
	
	/**
	 * Deletes article
	 * @param article
	 * @throws Exception
	 */
	public void deleteArticle(Article article) throws Exception {
		articleRepository.delete(article);
	}
	
	
	
	
	
	
	
	
	
	
	

//	/**
//	 * Gets article list.
//	 * 
//	 * @param pageInfo
//	 * @param searchType
//	 * @param searchValue
//	 * @return
//	 * @throws Exception
//	 */
//	public enum ArticleSearchType {
//		TITLE,
//		TITLE_CONTENTS,
//		USER
//	}
//	
//	/**
//	 * Gets articles
//	 * @param page
//	 * @return
//	 * @throws Exception
//	 */
//	public List<Article> getArticles(PageInfo pageInfo, String boardId, String categoryId, ArticleSearchType searchType, String searchValue) throws Exception {
//		Pageable pageable = pageInfo.toPageable();
//		Page<Article> articlesPage = null;
//		if(categoryId == null || categoryId.trim().length() < 1) {
//			if(searchType == null) {
//				articlesPage = articleRepository.findByBoardIdOrderByRegistDateDesc(boardId, pageable);			
//			}else {
//				switch(searchType) {
//					case TITLE :
//						articlesPage = articleRepository.findByBoardIdAndTitleContainingOrderByRegistDateDesc(boardId, searchValue, pageable);
//					break;
//					case TITLE_CONTENTS :
//						articlesPage = articleRepository.findByBoardIdAndTitleContainingOrContentsContainingOrderByRegistDateDesc(boardId, searchValue, searchValue, pageable);	
//					break;
//					case USER :
//						articlesPage = articleRepository.findByBoardIdAndUserIdContainingOrUserNicknameContainingOrderByRegistDateDesc(boardId, searchValue, searchValue, pageable);	
//					break;
//				}
//			}
//		}else {
//			if(searchType == null) {
//				articlesPage = articleRepository.findByBoardIdAndCategoryIdOrderByRegistDateDesc(boardId, categoryId, pageable);			
//			}else {
//				switch(searchType) {
//					case TITLE :
//						articlesPage = articleRepository.findByBoardIdAndCategoryIdAndTitleContainingOrderByRegistDateDesc(boardId, categoryId, searchValue, pageable);
//					break;
//					case TITLE_CONTENTS :
//						articlesPage = articleRepository.findByBoardIdAndCategoryIdAndTitleContainingOrContentsContainingOrderByRegistDateDesc(boardId, categoryId, searchValue, searchValue, pageable);	
//					break;
//					case USER :
//						articlesPage = articleRepository.findByBoardIdAndCategoryIdAndUserIdContainingOrUserNicknameContainingOrderByRegistDateDesc(boardId, categoryId, searchValue, searchValue, pageable);	
//					break;
//				}
//			}
//		}
//		if(pageInfo.isEnableTotalCount()) {
//			pageInfo.setTotalCount(articlesPage.getTotalElements());
//		}
//		List<Article> articles = articlesPage.getContent();
//		return articles;
//	}
//
//	/**
//	 * Gets article
//	 * 
//	 * @param id
//	 * @return
//	 * @throws Exception
//	 */
//	public Article getArticle(String id) throws Exception {
//		Article article = articleRepository.findOne(id);
//		article.setEntityManager(entityManager);
//		return article;
//	}
//
//	/**
//	 * Saves article
//	 * 
//	 * @param article
//	 * @throws Exception
//	 */
//	public Article saveArticle(Article article) throws Exception {
//
//		// In case of new article(articleNo is empty)
//		if (StringUtility.isEmpty(article.getId())) {
//			article.setId(EncodeUtility.generateUUID());
//			article.setRegistDate(new Date());
//			return articleRepository.save(article);
//		}
//		// In case of existing article updates
//		else {
//			Article one = articleRepository.findOne(article.getId());
//			one.setEntityManager(entityManager);
//			one.setTitle(article.getTitle());
//			one.setContents(article.getContents());
//			one.setModifyDate(new Date());
//
//			// add new file
//			for (ArticleFile file : article.getFiles()) {
//				if (one.getFile(file.getId()) == null) {
//					file.setArticleId(article.getId());
//					one.getFiles().add(file);
//
//					// move file from temporary directory
//					try {
//						FileUtils.moveFile(file.getTemporaryFile(), file.getRealFile());
//					} catch (Exception ignore) {
//						LOGGER.warn(ignore.getArticle(), ignore);
//					}
//				}
//			}
//
//			// remove deleted file
//			for (int index = one.getFiles().size() - 1; index >= 0; index--) {
//				ArticleFile oneFile = one.getFiles().get(index);
//				if(article.getFile(oneFile.getId()) == null) {
//					one.removeFile(oneFile.getId());
//					FileUtils.deleteQuietly(oneFile.getRealFile());
//				}
//			}
//
//			// saves data
//			return articleRepository.save(one);
//		}
//	}
//
//	/**
//	 * Deletes article
//	 * 
//	 * @throws Exception
//	 */
//	public void deleteArticle(Article article) throws Exception {
//		
//		// creates jpaRepositoryFactory
//		JpaRepositoryFactory jpaRepositoryFactory = new JpaRepositoryFactory(entityManager);
//
//		// deletes replies
//		ArticleReplyRepository articleReplyRepository = jpaRepositoryFactory.getRepository(ArticleReplyRepository.class);
//		for (ArticleReply reply : article.getReplies()) {
//			articleReplyRepository.delete(reply);
//		}
//
//		// deletes attached files.
//		ArticleFileRepository articleFileRepository = jpaRepositoryFactory.getRepository(ArticleFileRepository.class);
//		for (ArticleFile file : article.getFiles()) {
//			articleFileRepository.delete(file);
//			FileUtils.deleteQuietly(file.getRealFile());
//		}
//
//		// deletes entity
//		articleRepository.delete(article);
//	}
//	
//	/**
//	 * Gets latest articles
//	 * @param pageInfo
//	 * @return
//	 * @throws Exception
//	 */
//	public List<Article> getLatestArticles(PageInfo pageInfo, String boardId) throws Exception {
//		PageRowBounds rowBounds = pageInfo.toPageRowBounds();
//		List<Article> latestArticles = articleMapper.selectLatestArticles(boardId, rowBounds);
//		if(pageInfo.isEnableTotalCount()) {
//			pageInfo.setTotalCount(rowBounds.getTotalCount());
//		}
//		return latestArticles;
//	}
//
//	/**
//	 * Gets best articles has max read count
//	 * @param pageInfo
//	 * @return
//	 * @throws Exception
//	 */
//	public List<Article> getBestArticles(PageInfo pageInfo, String boardId) throws Exception {
//		PageRowBounds rowBounds = pageInfo.toPageRowBounds();
//		List<Article> bestArticles = articleMapper.selectBestArticles(boardId, rowBounds);
//		if(pageInfo.isEnableTotalCount()) {
//			pageInfo.setTotalCount(rowBounds.getTotalCount());
//		}
//		return bestArticles;
//	}

}
