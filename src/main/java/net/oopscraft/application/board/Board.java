package net.oopscraft.application.board;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.SQLInsert;
import org.hibernate.annotations.Where;
import org.hibernate.annotations.WhereJoinTable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;

import net.oopscraft.application.board.repository.BoardArticleRepository;
import net.oopscraft.application.core.PageInfo;
import net.oopscraft.application.user.Authority;


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
	
	@Column(name = "SKIN_ID")
	String skinId;
	
	public enum Policy {
		ANONYMOUS, AUTHENTICATED, AUTHORIZED
	}

	@Column(name = "ACES_PLCY")
	@Enumerated(EnumType.STRING)
	Policy accessPolicy = Policy.ANONYMOUS;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "APP_BORD_AUTH_MAP", joinColumns = @JoinColumn(name = "BORD_ID"), inverseJoinColumns = @JoinColumn(name = "AUTH_ID"))
	@WhereJoinTable(clause = "AUTH_TYPE ='ACES_PLCY'")
	@SQLInsert(sql = "INSERT INTO APP_BORD_AUTH_MAP (BORD_ID, AUTH_TYPE, AUTH_ID) VALUES (?, 'ACES_PLCY', ?)") 
	List<Authority> accessAuthorities = new ArrayList<Authority>();

	@Column(name = "READ_PLCY")
	@Enumerated(EnumType.STRING)
	Policy readPolicy = Policy.ANONYMOUS;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "APP_BORD_AUTH_MAP", joinColumns = @JoinColumn(name = "BORD_ID"), inverseJoinColumns = @JoinColumn(name = "AUTH_ID"))
	@WhereJoinTable(clause = "AUTH_TYPE ='READ_PLCY'")
	@SQLInsert(sql = "INSERT INTO APP_BORD_AUTH_MAP (BORD_ID, AUTH_TYPE, AUTH_ID) VALUES (?, 'READ_PLCY', ?)")
	List<Authority> readAuthorities = new ArrayList<Authority>();
	
	@Column(name = "WRIT_PLCY")
	@Enumerated(EnumType.STRING)
	Policy writePolicy = Policy.ANONYMOUS;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "APP_BORD_AUTH_MAP", joinColumns = @JoinColumn(name = "BORD_ID"), inverseJoinColumns = @JoinColumn(name = "AUTH_ID"))
	@WhereJoinTable(clause = "AUTH_TYPE ='WRIT_PLCY'")
	@SQLInsert(sql = "INSERT INTO APP_BORD_AUTH_MAP (BORD_ID, AUTH_TYPE, AUTH_ID) VALUES (?, 'WRIT_PLCY', ?)")
	List<Authority> writeAuthorities = new ArrayList<Authority>();
	
	@Column(name = "ROWS_PER_PAGE")
	int rowsPerPage = 10;
	
	@Column(name = "CATE_USE_YN")
	String categoryUseYn;
	
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
	public List<BoardArticle> getArticles(PageInfo pageInfo, String categoryId, ArticleSearchType searchType, String searchValue) throws Exception {
		BoardArticleRepository boardArticleRepository = new JpaRepositoryFactory(entityManager).getRepository(BoardArticleRepository.class);
		Pageable pageable = pageInfo.toPageable();
		Page<BoardArticle> boardArticlesPage = null;
		if(categoryId == null || categoryId.trim().length() < 1) {
			if(searchType == null) {
				boardArticlesPage = boardArticleRepository.findByBoardIdOrderByNoDesc(id, pageable);			
			}else {
				switch(searchType) {
					case TITLE :
						boardArticlesPage = boardArticleRepository.findByBoardIdAndTitleContainingOrderByNoDesc(id, searchValue, pageable);
					break;
					case TITLE_CONTENTS :
						boardArticlesPage = boardArticleRepository.findByBoardIdAndTitleContainingOrContentsContainingOrderByNoDesc(id, searchValue, searchValue, pageable);	
					break;
					case USER :
						boardArticlesPage = boardArticleRepository.findByBoardIdAndUserIdContainingOrUserNicknameContainingOrderByNoDesc(id, searchValue, searchValue, pageable);	
					break;
				}
			}
		}else {
			if(searchType == null) {
				boardArticlesPage = boardArticleRepository.findByBoardIdAndCategoryIdOrderByNoDesc(id, categoryId, pageable);			
			}else {
				switch(searchType) {
					case TITLE :
						boardArticlesPage = boardArticleRepository.findByBoardIdAndCategoryIdAndTitleContainingOrderByNoDesc(id, categoryId, searchValue, pageable);
					break;
					case TITLE_CONTENTS :
						boardArticlesPage = boardArticleRepository.findByBoardIdAndCategoryIdAndTitleContainingOrContentsContainingOrderByNoDesc(id, categoryId, searchValue, searchValue, pageable);	
					break;
					case USER :
						boardArticlesPage = boardArticleRepository.findByBoardIdAndCategoryIdAndUserIdContainingOrUserNicknameContainingOrderByNoDesc(id, categoryId, searchValue, searchValue, pageable);	
					break;
				}
			}
		}
		if(pageInfo.isEnableTotalCount()) {
			pageInfo.setTotalCount(boardArticlesPage.getTotalElements());
		}
		List<BoardArticle> boardArticles = boardArticlesPage.getContent();
		return boardArticles;
	}
	
	/**
	 * Gets article
	 * @param no
	 * @return
	 * @throws Exception
	 */
	public BoardArticle getArticle(long no) throws Exception {
		BoardArticleRepository boardArticleRepository = new JpaRepositoryFactory(entityManager).getRepository(BoardArticleRepository.class);
		BoardArticle article = boardArticleRepository.findOne(no);
		article.setEntityManager(entityManager);
		return article;
	}
	
	/**
	 * Saves article
	 * @param article
	 * @return
	 * @throws Exception
	 */
	public void saveArticle(BoardArticle article) throws Exception {
		article.setEntityManager(entityManager);
		article.save();
	}
	
	/**
	 * Deletes article
	 * @param no
	 * @return
	 * @throws Exception
	 */
	public void deleteArticle(long no) throws Exception {
		BoardArticleRepository boardArticleRepository = new JpaRepositoryFactory(entityManager).getRepository(BoardArticleRepository.class);
		BoardArticle boardArticle = boardArticleRepository.findOne(no);
		boardArticle.setEntityManager(entityManager);
		boardArticle.delete();
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

	public List<Authority> getAccessAuthorities() {
		return accessAuthorities;
	}

	public void setAccessAuthorities(List<Authority> accessAuthorities) {
		this.accessAuthorities = accessAuthorities;
	}

	public Policy getReadPolicy() {
		return readPolicy;
	}

	public List<Authority> getReadAuthorities() {
		return readAuthorities;
	}

	public void setReadAuthorities(List<Authority> readAuthorities) {
		this.readAuthorities = readAuthorities;
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
	
	public List<Authority> getWriteAuthorities() {
		return writeAuthorities;
	}

	public void setWriteAuthorities(List<Authority> writeAuthorities) {
		this.writeAuthorities = writeAuthorities;
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
