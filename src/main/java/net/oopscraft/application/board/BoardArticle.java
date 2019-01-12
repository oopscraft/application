package net.oopscraft.application.board;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;

import net.oopscraft.application.article.Article;

@Entity
@Table(name = "APP_BORD_ATCL_MAP")
public class BoardArticle extends Article {

	@Column(name = "BORD_ID")
	String boardId;
	
	@Column(name = "CATE_ID")
	String categoryId;
	
	@Formula("(SELECT A.CATE_NAME FROM APP_BORD_CATE_INFO A WHERE A.BORD_ID = BORD_ID AND A.CATE_ID = CATE_ID)")
	String categoryName;
	
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
	
}
