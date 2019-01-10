package net.oopscraft.application.board;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import net.oopscraft.application.article.Article;

@Entity
@Table(name = "APP_BORD_ATCL_MAP")
public class BoardArticle extends Article {

	@Column(name = "BORD_ID")
	String boardId;
	
	@Column(name = "CATE_ID")
	String categoryId;
	
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
	
	public void save() throws Exception {
		System.out.println("BoardArticle.save()");
		super.save();
	}
	
	public void delete() throws Exception {
		System.out.println("BoardArticle.delete()");
		super.delete();
	}
	
}
