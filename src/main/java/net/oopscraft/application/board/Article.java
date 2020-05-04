package net.oopscraft.application.board;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Formula;

import com.fasterxml.jackson.annotation.JsonView;

import net.oopscraft.application.core.jpa.BooleanStringConverter;
import net.oopscraft.application.core.jpa.SystemEntity;

@Entity
@Table(name="APP_ATCL_INFO")
public class Article extends SystemEntity {
	
	@Id
	@Column(name="ATCL_ID", length=32)
	@NotNull
	@JsonView(List.class)
	String id;
	
	@Column(name="ATCL_TITL", length=4000)
	@NotNull
	@JsonView(List.class)
	String title;
	
	@Column(name = "ATCL_DATE")
	@JsonView(List.class)
	Date date;
	
	@Column(name="ATCL_ATHR", length = 1024)
	String author;
	
	@Column(name="ATCL_PASS", length = 1024)
	String password;
	
	public enum Format { HTML, MARKDOWN }
	@Column(name = "ATCL_FOMT", length = 64)
	@Enumerated(EnumType.STRING)
	@JsonView(List.class)
	Format format;
	
	@Column(name="ATCL_CNTS", length=Integer.MAX_VALUE)
	@Lob
	String contents;
	
	@Column(name = "BORD_ID", length = 32)
	@JsonView(List.class)
	String boardId;
	
	@Column(name = "CATE_ID", length = 32)
	@JsonView(List.class)
	String categoryId;

	@Formula("(select a.CATE_NAME from APP_BORD_CATE_INFO a where a.BORD_ID = BORD_ID and a.CATE_ID = CATE_ID)")
	@JsonView(List.class)
	String categoryName;

	@Column(name = "NOTI_YN")
	@Convert(converter=BooleanStringConverter.class)
	@JsonView(List.class)
	boolean notice;
	
	@Column(name="USER_ID", length=32)
	@JsonView(List.class)
	String userId;

	@Formula("(select count(*) from APP_ATCL_FILE_INFO a where a.ATCL_ID = ATCL_ID)")
	@JsonView(List.class)
	int fileCount;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "articleId", cascade = CascadeType.ALL)
	List<ArticleFile> files = new ArrayList<ArticleFile>();
	
	@Formula("(select count(*) from APP_ATCL_RPLY_INFO a where a.ATCL_ID = ATCL_ID)")
	int replyCount;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "articleId", cascade = CascadeType.ALL)
	@OrderBy("date")
	List<ArticleReply> replies = new ArrayList<ArticleReply>();
	
	public Article() {}
	
	public Article(String id) {
		this.id=id;
	}

	/**
	 * Returns file by id
	 * @param fileId
	 * @return
	 */
	public ArticleFile getFile(String fileId) {
		for(ArticleFile file : files) {
			if(fileId.contentEquals(file.getId())) {
				return file;
			}
		}
		return null;
	}
	
	/**
	 * Adds file
	 * @param file
	 */
	public void addFile(ArticleFile file) {
		this.files.add(file);
	}
	
	/**
	 * Removes file by id
	 * @param fileId
	 * @return
	 */
	public boolean removeFile(String fileId) {
		for(ArticleFile file : files) {
			if(fileId.contentEquals(file.getId())) {
				files.remove(file);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * getReply
	 * @param replyId
	 * @return
	 */
	public ArticleReply getReply(String replyId) {
		for(ArticleReply reply : replies) {
			if(replyId.contentEquals(reply.getId())) {
				return reply;
			}
		}
		return null;
	}
	
	/**
	 * addReply
	 * @param reply
	 */
	public void addReply(ArticleReply reply) {
		this.replies.add(reply);
	}
	
	/**
	 * removeReply
	 * @param replyId
	 * @return
	 */
	public boolean removeReply(String replyId) {
		for(ArticleReply reply : replies) {
			if(replyId.contentEquals(reply.getId())) {
				replies.remove(reply);
				return true;
			}
		}
		return false;
	}
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id=id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title=title;
	}
	
	public Format getFormat() {
		return format;
	}

	public void setFormat(Format format) {
		this.format = format;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents=contents;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getFileCount() {
		return fileCount;
	}

	public void setFileCount(int fileCount) {
		this.fileCount = fileCount;
	}

	public List<ArticleReply> getReplies() {
		return replies;
	}

	public void setReplies(List<ArticleReply> replies) {
		this.replies = replies;
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
	
	public String getBoardId() {
		return boardId;
	}

	public void setBoardId(String boardId) {
		this.boardId = boardId;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public boolean isNotice() {
		return notice;
	}

	public void setNotice(boolean notice) {
		this.notice = notice;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
