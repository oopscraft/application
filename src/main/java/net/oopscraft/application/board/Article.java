package net.oopscraft.application.board;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.Formula;

@Entity
@Table(name = "APP_ATCL_INFO")
public class Article {

	@Id
	@Column(name = "ATCL_NO")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "hibernate_sequence")
	@TableGenerator(name = "hibernate_sequence", allocationSize = 1)
	long no;

	@Column(name = "BORD_ID")
	String boardId;
	
	@Column(name = "CATE_ID")
	String categoryId;
	
	@Column(name = "ATCL_TITL")
	String title;

	@Column(name = "ATCL_CNTS")
	String contents;

	@Column(name = "ATCL_USER_ID")
	String userId;
	
	@Formula("(SELECT A.USER_NICK FROM APP_USER_INFO A WHERE A.USER_ID = ATCL_USER_ID)")
	String userNickname;
	
	@Formula("(SELECT A.USER_AVAT FROM APP_USER_INFO A WHERE A.USER_ID = ATCL_USER_ID)")
	String userAvatar;
	
	@Column(name = "ATCL_RGST_DTTM")
	Date registDate;
	
	@Column(name = "ATCL_MDFY_DTTM")
	Date modifyDate;
	
	@Column(name = "READ_CNT")
	int readCount;
	
	@Column(name = "VOTE_PSTV_CNT")
	int votePositiveCount;
	
	@Column(name = "VOTE_NGTV_CNT")
	int voteNegativeCount;
	
	@Formula("(SELECT COUNT(*)FROM APP_ATCL_RPLY_INFO A WHERE A.ATCL_NO = ATCL_NO)")
	int replyCount;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "articleNo", cascade = CascadeType.ALL)
	List<ArticleReply> replies = new ArrayList<ArticleReply>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "articleNo", cascade = CascadeType.ALL)
	List<ArticleFile> files = new ArrayList<ArticleFile>();

	public String getBoardId() {
		return boardId;
	}

	public void setBoardId(String boardId) {
		this.boardId = boardId;
	}

	public long getNo() {
		return no;
	}

	public void setNo(long no) {
		this.no = no;
	}
	
	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
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

	public int getVotePositiveCount() {
		return votePositiveCount;
	}

	public void setVotePositiveCount(int votePositiveCount) {
		this.votePositiveCount = votePositiveCount;
	}

	public int getVoteNegativeCount() {
		return voteNegativeCount;
	}

	public void setVoteNegativeCount(int voteNegativeCount) {
		this.voteNegativeCount = voteNegativeCount;
	}
	
	public int getReplyCount() {
		return replyCount;
	}

	public void setReplyCount(int replyCount) {
		this.replyCount = replyCount;
	}

	public List<ArticleReply> getReplies() {
		return replies;
	}

	public void setReplies(List<ArticleReply> replies) {
		this.replies = replies;
	}

	public List<ArticleFile> getFiles() {
		return files;
	}

	public void setFiles(List<ArticleFile> files) {
		this.files = files;
	}

}
