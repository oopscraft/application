package net.oopscraft.application.board.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;

import net.oopscraft.application.core.jpa.SystemEntity;

@Entity
@Table(name="APP_ATCL_INFO")
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class Article extends SystemEntity {

	@Id
	@Column(name="ATCL_ID", length=32)
	String id;
	
	@Column(name="ATCL_TITL", length=4000)
	String title;

	@Column(name="ATCL_CNTS", length=Integer.MAX_VALUE)
	@Lob
	String contents;
	
	@Column(name="USER_ID", length=32)
	String userId;

	@Formula("(select count(*) from APP_ATCL_FILE_INFO a where a.ATCL_ID = ATCL_ID)")
	int fileCount;
	
	@OneToMany(
		fetch = FetchType.LAZY, 
		mappedBy = "articleId",
		cascade = CascadeType.ALL, 
		orphanRemoval = true
	)
	List<ArticleFile> files = new ArrayList<ArticleFile>();
	
	@Formula("(select count(*) from APP_ATCL_RPLY_INFO a where a.ATCL_ID = ATCL_ID)")
	int replyCount;
	
	@OneToMany(
		fetch = FetchType.LAZY, 
		mappedBy = "articleId",
		cascade = CascadeType.ALL, 
		orphanRemoval = true
	)
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
	 * Adds file
	 * @param file
	 */
	public void addFile(ArticleFile file) {
		this.files.add(file);
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
	
}
