package net.oopscraft.application.board;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import net.oopscraft.application.core.IdGenerator;
import net.oopscraft.application.core.Pagination;

@Service
public class ArticleService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ArticleService.class);
	
    @Value("${application.upload.path}")
    String uploadPath;

	@Autowired
	ArticleRepository articleRepository;
	
	/**
	 * Returns board articles
	 * @param code
	 * @param pagination
	 * @return
	 * @throws Exception
	 */
	public List<Article> getArticles(final Article article, Pagination pagination) throws Exception {
		List<Order> orders = new ArrayList<Order>();
		orders.add(new Order(Direction.DESC, "notice"));
		orders.add(new Order(Direction.DESC, "date"));
		PageRequest pageRequest = pagination.toPageRequest(new Sort(orders));
		Page<Article> boardArticlesPage = articleRepository.findAll(new Specification<Article>() {
			@Override
			public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				if(!StringUtils.isBlank(article.getBoardId())) {
					predicates.add(cb.and(cb.equal(root.get("boardId").as(String.class), article.getBoardId())));
				}
				if(!StringUtils.isBlank(article.getCategoryId())) {
					predicates.add(cb.and(cb.equal(root.get("categoryId").as(String.class), article.getCategoryId())));
				}
				if(!StringUtils.isBlank(article.getTitle())) {
					predicates.add(cb.and(cb.like(root.get("title").as(String.class), '%'+ article.getTitle()+'%')));
				}
				if(!StringUtils.isBlank(article.getContents())) {
					predicates.add(cb.and(cb.like(root.get("contents").as(String.class), '%'+ article.getContents()+'%')));
				}
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		}, pageRequest);
		pagination.setTotalCount(boardArticlesPage.getTotalElements());
		return boardArticlesPage.getContent();
	}
	
	/**
	 * Gets board article
	 * @param boardId
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Article getArticle(String id) throws Exception {
		return articleRepository.findOne(id);
	}
	
	/**
	 * Returns board article
	 * @param article
	 * @return
	 * @throws Exception
	 */
	public Article saveArticle(Article article) throws Exception {
		
		Article one = null;
		if(article.getId()==null 
		||(one = articleRepository.findOne(article.getId())) == null) {
			one = new Article();
			one.setId(IdGenerator.uuid());
			one.setAuthor(article.getAuthor());
			one.setDate(new Date());
			one.setUserId(article.getUserId());
		}
//		one.setBoardId(article.getBoardId());
//		one.setCategoryId(article.getCategoryId());
		one.setTitle(article.getTitle());
		one.setFormat(article.getFormat());
		one.setContents(article.getContents());
		
		// add new file
		for (ArticleFile file : article.getFiles()) {
			if(one.getFile(file.getId()) == null) {
				file.setArticleId(one.getId());
				one.getFiles().add(file);
				File temporaryFile = new File(uploadPath + File.separator + file.getId());
				File realFile = new File(uploadPath + File.separator + "board" + File.separator + file.getId());
				try {
					FileUtils.moveFile(temporaryFile, realFile);
				} catch (Exception ignore) {
					LOGGER.warn(ignore.getMessage(), ignore);
				}
			}
		}

		// remove deleted file
		for (int index = one.getFiles().size() - 1; index >= 0; index--) {
			ArticleFile file = one.getFiles().get(index);
			if(article.getFile(file.getId()) == null) {
				one.removeFile(file.getId());
				File realFile = new File(uploadPath + File.separator + "board" + File.separator + file.getId());
				FileUtils.deleteQuietly(realFile);
			}
		}
		
		// saves
		return articleRepository.save(one);
	}
	
	/**
	 * Deletes board article
	 * @param code
	 * @throws Exception
	 */
	public void deleteArticle(Article article) throws Exception {
		Article one = articleRepository.findOne(article.getId());
		for(ArticleFile articleFile : one.getFiles()) {
			File file = new File(uploadPath + File.separator + "board" + File.separator + articleFile.getId());
			FileUtils.deleteQuietly(file);
		}
		articleRepository.delete(one);
	}
	
}
