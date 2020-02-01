package net.oopscraft.application.article.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Table(name = "APP_ATCL_INFO")
@Inheritance(
	strategy = InheritanceType.JOINED
)
public class Article {
	
	@Id
	@Column(name = "ATCL_ID")
	String id;
	
	@Column(name = "ATCL_TITL")
	String title;

	@Column(name = "ATCL_CNTS")
	String contents;

	@Column(name = "USER_ID")
	String userId;
	
	@Column(name = "WRIT_DATE")
	Date writeDate;
	
	@Column(name = "MDFY_DATE")
	Date modifyDate;
	
	public Article() {}
	
	public Article(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public Date getWriteDate() {
		return writeDate;
	}

	public void setWriteDate(Date writeDate) {
		this.writeDate = writeDate;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	
	
	
}
