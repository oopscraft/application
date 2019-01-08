package net.oopscraft.application.board;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;

import net.oopscraft.application.board.repository.ArticleReplyRepository;
import net.oopscraft.application.board.repository.ArticleRepository;
import net.oopscraft.application.core.PageInfo;


@Entity
@Table(name = "APP_BORD_INFO")
public class Board {
	
	private static Logger LOGGER = LoggerFactory.getLogger(Board.class);

	@Id
	@Column(name = "BORD_ID")
	String id;
	
	@Column(name = "BORD_NAME")
	String name;
	
	@Column(name = "LAYT_ID")
	String layoutId;
	
	@Column(name = "SKIN_ID")
	String skinId;
	
	public enum Policy {
		ANONYMOUS, AUTHENTICATED, AUTHORIZED
	}

	@Column(name = "ACES_PLCY")
	@Enumerated(EnumType.STRING)
	Policy accessPolicy = Policy.ANONYMOUS;

	@Column(name = "READ_PLCY")
	@Enumerated(EnumType.STRING)
	Policy readPolicy = Policy.ANONYMOUS;
	
	@Column(name = "WRIT_PLCY")
	@Enumerated(EnumType.STRING)
	Policy writePolicy = Policy.ANONYMOUS;
	
	@Column(name = "ROWS_PER_PAGE")
	int rowsPerPage = 10;
	
	@Column(name = "RPLY_USE_YN")
	String replyUseYn;
	
	@Column(name = "FILE_USE_YN")
	String fileUseYn;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "boardId", cascade = CascadeType.ALL)
	@OrderBy("displaySeq")
	List<BoardCategory> categories = new ArrayList<BoardCategory>();
	
	@Transient
	EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

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
	public List<Article> getArticles(PageInfo pageInfo, String categoryId, ArticleSearchType searchType, String searchValue) throws Exception {
		ArticleRepository articleRepository = new JpaRepositoryFactory(entityManager).getRepository(ArticleRepository.class);
		Pageable pageable = pageInfo.toPageable();
		Page<Article> articlesPage = null;
		if(categoryId == null || categoryId.trim().length() < 1) {
			if(searchType == null) {
				articlesPage = articleRepository.findByBoardIdOrderByNoDesc(id, pageable);			
			}else {
				switch(searchType) {
					case TITLE :
						articlesPage = articleRepository.findByBoardIdAndTitleContainingOrderByNoDesc(id, searchValue, pageable);
					break;
					case TITLE_CONTENTS :
						articlesPage = articleRepository.findByBoardIdAndTitleContainingOrContentsContainingOrderByNoDesc(id, searchValue, searchValue, pageable);	
					break;
					case USER :
						articlesPage = articleRepository.findByBoardIdAndUserIdContainingOrUserNicknameContainingOrderByNoDesc(id, searchValue, searchValue, pageable);	
					break;
				}
			}
		}else {
			if(searchType == null) {
				articlesPage = articleRepository.findByBoardIdAndCategoryIdOrderByNoDesc(id, categoryId, pageable);			
			}else {
				switch(searchType) {
					case TITLE :
						articlesPage = articleRepository.findByBoardIdAndCategoryIdAndTitleContainingOrderByNoDesc(id, categoryId, searchValue, pageable);
					break;
					case TITLE_CONTENTS :
						articlesPage = articleRepository.findByBoardIdAndCategoryIdAndTitleContainingOrContentsContainingOrderByNoDesc(id, categoryId, searchValue, searchValue, pageable);	
					break;
					case USER :
						articlesPage = articleRepository.findByBoardIdAndCategoryIdAndUserIdContainingOrUserNicknameContainingOrderByNoDesc(id, categoryId, searchValue, searchValue, pageable);	
					break;
				}
			}
		}
		if(pageInfo.isEnableTotalCount()) {
			pageInfo.setTotalCount(articlesPage.getTotalElements());
		}
		List<Article> articles = articlesPage.getContent();
		return articles;
	}
	
	/**
	 * Gets article
	 * @param no
	 * @return
	 * @throws Exception
	 */
	public Article getArticle(long no) throws Exception {
		ArticleRepository articleRepository = new JpaRepositoryFactory(entityManager).getRepository(ArticleRepository.class);
		Article article = articleRepository.findOne(no);
		article.setEntityManager(entityManager);
		return article;
	}
	
	/**
	 * Saves article
	 * @param article
	 * @return
	 * @throws Exception
	 */
	public Article saveArticle(Article article) throws Exception {
		ArticleRepository articleRepository = new JpaRepositoryFactory(entityManager).getRepository(ArticleRepository.class);
		
		// In case of new article(articleNo is empty)
		if(article.getNo() < 1) {
			article.setBoardId(id);
			article.setRegistDate(new Date());
			article.setReadCount(0);
			return articleRepository.saveAndFlush(article);
		}
		// In case of existing article updates
		else {
			Article one = articleRepository.findOne(article.getNo());
			one.setTitle(article.getTitle());
			one.setContents(article.getContents());
			one.setModifyDate(new Date());
			
			// add new file
			for(ArticleFile file : article.getFiles()) {
				if(one.getFile(file.getId()) == null) {
					file.setArticleNo(article.getNo());
					one.getFiles().add(file);
					
					// move file from temporary directory
					try {
						FileUtils.moveFile(file.getTemporaryFile(), file.getRealFile());
					}catch(Exception ignore) {
						LOGGER.warn(ignore.getMessage(), ignore);
					}
				}
			}
			
			// remove deleted file
			for(int index = one.getFiles().size()-1; index >= 0; index --) {
				ArticleFile file = one.getFiles().get(index);
				if(article.getFile(file.getId()) == null) {
					one.removeFile(file.getId());
					
					// remove real file.
					FileUtils.deleteQuietly(file.getRealFile());
				}
			}
			
			// saves data
			return articleRepository.saveAndFlush(one);
		}
	}
	
	/**
	 * Deletes article
	 * @param no
	 * @return
	 * @throws Exception
	 */
	public void deleteArticle(long no) throws Exception {
		ArticleRepository articleRepository = new JpaRepositoryFactory(entityManager).getRepository(ArticleRepository.class);
		Article article = articleRepository.findOne(no);
		article.setEntityManager(entityManager);
		
		// deletes replies
		ArticleReplyRepository articleReplyRepository = new JpaRepositoryFactory(entityManager).getRepository(ArticleReplyRepository.class);
		for(ArticleReply reply : article.getReplies()) {
			articleReplyRepository.delete(reply);	
		}
		
		// deletes attached files.
		for(ArticleFile file : article.getFiles()) {
			FileUtils.deleteQuietly(file.getRealFile());
		}
		
		// deletes entity
		articleRepository.delete(no);
	}
	
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

	public String getLayoutId() {
		return layoutId;
	}

	public void setLayoutId(String layoutId) {
		this.layoutId = layoutId;
	}

	public String getSkinId() {
		return skinId;
	}

	public void setSkinId(String skinId) {
		this.skinId = skinId;
	}

	public Policy getAccessPolicy() {
		return accessPolicy;
	}

	public void setAccessPolicy(Policy accessPolicy) {
		this.accessPolicy = accessPolicy;
	}

	public Policy getReadPolicy() {
		return readPolicy;
	}

	public void setReadPolicy(Policy readPolicy) {
		this.readPolicy = readPolicy;
	}

	public Policy getWritePolicy() {
		return writePolicy;
	}

	public void setWritePolicy(Policy writePolicy) {
		this.writePolicy = writePolicy;
	}
	
	public int getRowsPerPage() {
		return rowsPerPage;
	}

	public void setRowsPerPage(int rowsPerPage) {
		this.rowsPerPage = rowsPerPage;
	}

	public String getReplyUseYn() {
		return replyUseYn;
	}

	public void setReplyUseYn(String replyUseYn) {
		this.replyUseYn = replyUseYn;
	}

	public String getFileUseYn() {
		return fileUseYn;
	}

	public void setFileUseYn(String fileUseYn) {
		this.fileUseYn = fileUseYn;
	}

	public List<BoardCategory> getCategories() {
		return categories;
	}

	public void setCategories(List<BoardCategory> categories) {
		this.categories = categories;
	}

}
