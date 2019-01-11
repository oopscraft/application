package net.oopscraft.application.article;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.stereotype.Service;

import net.oopscraft.application.article.repository.ArticleFileRepository;
import net.oopscraft.application.article.repository.ArticleReplyRepository;
import net.oopscraft.application.article.repository.ArticleRepository;
import net.oopscraft.application.core.PageInfo;

@Service
public class ArticleService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ArticleService.class);

	@PersistenceContext
	EntityManager entityManager;

	@Autowired
	ArticleRepository articleRepository;

	public enum ArticleSearchType {
		TITLE, TITLE_CONTENTS, USER
	}

	/**
	 * Gets article list.
	 * 
	 * @param pageInfo
	 * @param searchType
	 * @param searchValue
	 * @return
	 * @throws Exception
	 */
	public List<Article> getArticles(PageInfo pageInfo, ArticleSearchType searchType, String searchValue)
			throws Exception {
		Pageable pageable = pageInfo.toPageable();
		Page<Article> articlesPage = articleRepository.findAll(pageable);
		if (pageInfo.isEnableTotalCount()) {
			pageInfo.setTotalCount(articlesPage.getTotalElements());
		}
		List<Article> articles = articlesPage.getContent();
		return articles;
	}

	/**
	 * Gets article
	 * 
	 * @param no
	 * @return
	 * @throws Exception
	 */
	public Article getArticle(long no) throws Exception {
		Article article = articleRepository.findOne(no);
		return article;
	}

	/**
	 * Saves article
	 * 
	 * @param article
	 * @throws Exception
	 */
	public void saveArticle(Article article) throws Exception {

		// In case of new article(articleNo is empty)
		if (article.getNo() < 1) {
			articleRepository.saveAndFlush(article);
		}
		// In case of existing article updates
		else {
			Article one = articleRepository.findOne(article.getNo());
			one.setEntityManager(entityManager);
			one.setTitle(article.getTitle());
			one.setContents(article.getContents());
			one.setModifyDate(new Date());

			// add new file
			for (ArticleFile file : article.getFiles()) {
				if (one.getFile(file.getId()) == null) {
					file.setArticleNo(article.getNo());
					one.getFiles().add(file);

					// move file from temporary directory
					try {
						FileUtils.moveFile(file.getTemporaryFile(), file.getRealFile());
					} catch (Exception ignore) {
						LOGGER.warn(ignore.getMessage(), ignore);
					}
				}
			}

			// remove deleted file
			for (int index = one.getFiles().size() - 1; index >= 0; index--) {
				ArticleFile oneFile = one.getFiles().get(index);
				if(article.getFile(oneFile.getId()) == null) {
					one.removeFile(oneFile.getId());
					FileUtils.deleteQuietly(oneFile.getRealFile());
				}
			}

			// saves data
			articleRepository.saveAndFlush(one);
		}
	}

	/**
	 * Deletes article
	 * 
	 * @throws Exception
	 */
	public void deleteArticle(Article article) throws Exception {

		// deletes replies
		ArticleReplyRepository articleReplyRepository = new JpaRepositoryFactory(entityManager)
				.getRepository(ArticleReplyRepository.class);
		for (ArticleReply reply : article.getReplies()) {
			articleReplyRepository.delete(reply);
		}

		// deletes attached files.
		ArticleFileRepository articleFileRepository = new JpaRepositoryFactory(entityManager)
				.getRepository(ArticleFileRepository.class);
		for (ArticleFile file : article.getFiles()) {
			articleFileRepository.delete(file);
			FileUtils.deleteQuietly(file.getRealFile());
		}

		// deletes entity
		articleRepository.delete(article);
	}

}
