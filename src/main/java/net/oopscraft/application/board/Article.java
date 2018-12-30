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
	
	@Column(name = "ATCL_RGST_DTTM")
	Date registDate;
	
	@Column(name = "ATCL_MDFY_DTTM")
	Date modifyDate;

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

	public List<ArticleFile> getFiles() {
		return files;
	}

	public void setFiles(List<ArticleFile> files) {
		this.files = files;
	}

}
