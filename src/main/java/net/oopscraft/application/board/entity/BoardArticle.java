package net.oopscraft.application.board.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;

import net.oopscraft.application.article.entity.Article;
import net.oopscraft.application.core.jpa.BooleanStringConverter;

@Entity
@Table(name = "APP_BORD_ATCL_INFO")
public class BoardArticle extends Article {

	@Column(name = "BORD_ID", length = 32)
	String boardId;
	
	@Column(name = "CATE_ID", length = 32)
	String categoryId;
	
	@Formula("(select a.CATE_NAME from APP_BORD_CATE_INFO a where a.CATE_ID = CATE_ID)")
	String categoryName;
	
	@Column(name = "NOTI_YN")
	@Convert(converter=BooleanStringConverter.class)
	boolean notice;
	
	@Column(name = "REGI_DATE")
	Date registerDate;
	
	@Column(name="ATHR_NAME", length = 1024)
	String authorName;
	
	@Column(name="ATHR_PASS", length = 1024)
	String authorPassword;

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

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public String getAuthorPassword() {
		return authorPassword;
	}

	public void setAuthorPassword(String authorPassword) {
		this.authorPassword = authorPassword;
	}

	public Date getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}
	
}
