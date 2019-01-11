package net.oopscraft.application.article;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import org.hibernate.annotations.Formula;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;

import net.oopscraft.application.article.repository.ArticleReplyRepository;
import net.oopscraft.application.article.repository.ArticleRepository;

@Entity
@Table(name = "APP_ATCL_INFO")
@Inheritance(strategy = InheritanceType.JOINED)
public class Article {
    
	@Transient
	EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	@Id
	@Column(name = "ATCL_NO")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "hibernate_sequence")
	@TableGenerator(name = "hibernate_sequence", allocationSize = 1)
	long no;

	@Column(name = "ATCL_TITL")
	String title;

	@Column(name = "ATCL_CNTS")
	String contents;

	@Column(name = "ATCL_USER_ID")
	String userId;
	
	@Column(name = "ATCL_USER_NICK")
	String userNickname;
	
	@Formula("(SELECT A.USER_AVAT FROM APP_USER_INFO A WHERE A.USER_ID = ATCL_USER_ID)")
	String userAvatar;
	
	@Column(name = "ATCL_RGST_DTTM")
	Date registDate;
	
	@Column(name = "ATCL_MDFY_DTTM")
	Date modifyDate;
	
	@Column(name = "READ_CNT")
	int readCount;
	
	@Formula("(SELECT COUNT(*)FROM APP_ATCL_RPLY_INFO A WHERE A.ATCL_NO = ATCL_NO)")
	int replyCount;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "articleNo", cascade = CascadeType.ALL, orphanRemoval = true)
	List<ArticleFile> files = new ArrayList<ArticleFile>();

	/**
	 * Increases read count.
	 * @throws Exception
	 */
	public void increaseReadCount() throws Exception {
		ArticleRepository articleRepository = new JpaRepositoryFactory(entityManager).getRepository(ArticleRepository.class);
		articleRepository.increaseReadCount(no);
	}

	/**
	 * Gets replies.
	 * @return
	 * @throws Exception
	 */
	@JsonIgnore
	public List<ArticleReply> getReplies() throws Exception {
		ArticleReplyRepository articleReplyRepository = new JpaRepositoryFactory(entityManager).getRepository(ArticleReplyRepository.class);
		return articleReplyRepository.findByArticleNoOrderBySequenceAscLevelAsc(no);
	}
	
	/**
	 * Saves reply.
	 * @param reply
	 * @return
	 * @throws Exception
	 */
	public ArticleReply saveReply(ArticleReply reply) throws Exception {
		ArticleReplyRepository articleReplyRepository = new JpaRepositoryFactory(entityManager).getRepository(ArticleReplyRepository.class);
		if(reply.getNo() < 1) {
			reply.setArticleNo(no);
			
			// In case of child reply(has upper no)
			if(reply.getUpperNo() > 0) {
				ArticleReply upperArticleReply = articleReplyRepository.findOne(new ArticleReply.Pk(no,reply.getUpperNo()));
				reply.setSequence(upperArticleReply.getSequence());
				StringBuffer level = new StringBuffer();
				level.append(upperArticleReply.getLevel() == null ? "" : upperArticleReply.getLevel());
				String siblingMaxLevel = articleReplyRepository.getSiblingMaxLevel(reply.getUpperNo());
				if(siblingMaxLevel == null) {
					level.append("A");
				}else {
					char lastChar = siblingMaxLevel.charAt(siblingMaxLevel.length()-1);
					char newChar = (char)((int)lastChar + 1);
					level.append(newChar);
				}
				reply.setLevel(level.toString());
			}
			// just root reply	
			else {
				Integer maxSequence = articleReplyRepository.getMaxSequence(no);
				reply.setSequence((maxSequence == null ? 0 : maxSequence.intValue()) + 1);
				reply.setLevel("");
			}
			return articleReplyRepository.saveAndFlush(reply);
		}else {
			ArticleReply one = articleReplyRepository.findOne(new ArticleReply.Pk(no,reply.getNo()));
			one.setContents(reply.getContents());
			return articleReplyRepository.saveAndFlush(one);
		}
	}

	/**
	 * Deletes reply 
	 * @param replyNo
	 * @throws Exception
	 */
	public void deleteReply(long replyNo) throws Exception {
		ArticleReplyRepository articleReplyRepository = new JpaRepositoryFactory(entityManager).getRepository(ArticleReplyRepository.class);
		articleReplyRepository.delete(new ArticleReply.Pk(no,replyNo));
	}
	
	/**
	 * Deletes reply all
	 * @throws Exception
	 */
	public void deleteReplyAll() throws Exception {
		ArticleReplyRepository articleReplyRepository = new JpaRepositoryFactory(entityManager).getRepository(ArticleReplyRepository.class);
		articleReplyRepository.deleteByArticleNo(no);
	}
	
	/**
	 * Gets file.
	 * @param fileId
	 * @return
	 * @throws Exception
	 */
	public ArticleFile getFile(String fileId) throws Exception {
		for(ArticleFile file : getFiles()) {
			if(file.getId().equals(fileId) == true) {
				return file;
			}
		}
		return null;
	}
	
	/**
	 * Removes file.
	 * @param fileId
	 * @return
	 * @throws Exception
	 */
	public boolean removeFile(String fileId) throws Exception {
		for(ArticleFile file : getFiles()) {
			if(fileId.equals(file.getId()) == true) {
				return files.remove(file);
			}
		}
		return false;
	}

	
	public long getNo() {
		return no;
	}

	public void setNo(long no) {
		this.no = no;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getUserNickname() {
		return userNickname;
	}

	public void setUserNickname(String userNickname) {
		this.userNickname = userNickname;
	}
	
	public String getUserAvatar() {
		return userAvatar;
	}

	public void setUserAvatar(String userAvatar) {
		this.userAvatar = userAvatar;
	}

	public Date getRegistDate() {
		return registDate;
	}

	public void setRegistDate(Date registDate) {
		this.registDate = registDate;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public int getReadCount() {
		return readCount;
	}

	public void setReadCount(int readCount) {
		this.readCount = readCount;
	}

	public int getReplyCount() {
		return replyCount;
	}

	public void setReplyCount(int replyCount) {
		this.replyCount = replyCount;
	}

	public List<ArticleFile> getFiles() {
		return files;
	}

	public void setFiles(List<ArticleFile> files) {
		this.files = files;
	}
	
	
}
