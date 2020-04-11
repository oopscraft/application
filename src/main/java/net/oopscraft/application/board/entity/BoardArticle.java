package net.oopscraft.application.board.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;

import net.oopscraft.application.article.entity.Article;
import net.oopscraft.application.core.jpa.BooleanStringConverter;

@Entity
@Table(name = "APP_BORD_ATCL")
public class BoardArticle extends Article {

	@Column(name = "BORD_ID", length = 32)
	String boardId;
	
	@Column(name = "CATE_ID", length = 32)
	String categoryId;
	
	@Column(name = "NOTI_YN")
	@Convert(converter=BooleanStringConverter.class)
	boolean notice;
	
	@Column(name = "REGI_DATE")
	Date registrationDate;

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

	public boolean isNotice() {
		return notice;
	}

	public void setNotice(boolean notice) {
		this.notice = notice;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}
	
}
