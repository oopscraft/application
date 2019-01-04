package net.oopscraft.application.board;

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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;

import net.oopscraft.application.board.repository.ArticleRepository;
import net.oopscraft.application.core.PageInfo;


@Entity
@Table(name = "APP_BORD_INFO")
public class Board {

	@Id
	@Column(name = "BORD_ID")
	String id;
	
	@Column(name = "BORD_NAME")
	String name;
	
	@Column(name = "LAYT_ID")
	String layoutId;
	
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
	
	@Column(name = "PAGE_PER_ROWS")
	int listPerRows = 10;
	
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

	/**
	 * Gets articles
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<Article> getArticles(PageInfo pageInfo, String searchKey, String searchValue) throws Exception {
		ArticleRepository articleRepository = new JpaRepositoryFactory(entityManager).getRepository(ArticleRepository.class);
		Pageable pageable = pageInfo.toPageable();
		Page<Article> articlesPage = articleRepository.findByBoardIdOrderByNoDesc(id, pageable);
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
		if(article.getNo() < 1) {
			article.setBoardId(id);
			article.setRegistDate(new Date());
			article.setReadCount(0);
			article.setVotePositiveCount(0);
			article.setVoteNegativeCount(0);
			return articleRepository.saveAndFlush(article);
		}else {
			Article one = articleRepository.findOne(article.getNo());
			one.setTitle(article.getTitle());
			one.setContents(article.getContents());
			one.setModifyDate(new Date());
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
	
	public int getListPerRows() {
		return listPerRows;
	}

	public void setListPerRows(int listPerRows) {
		this.listPerRows = listPerRows;
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
