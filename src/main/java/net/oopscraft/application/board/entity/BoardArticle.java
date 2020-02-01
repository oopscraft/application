package net.oopscraft.application.board.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Table;

import net.oopscraft.application.article.entity.Article;

@Entity
@Table(name = "APP_BORD_ATCL_INFO")
@IdClass(BoardArticle.Pk.class)
public class BoardArticle extends Article {
	
	public static class Pk implements Serializable {
		private static final long serialVersionUID = -8468231238873447341L;
		String boardId;
		String id;
		public String getBoardId() {
			return boardId;
		}
		public void setBoardId(String boardId) {
			this.boardId = boardId;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((boardId == null) ? 0 : boardId.hashCode());
			result = prime * result + ((id == null) ? 0 : id.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Pk other = (Pk) obj;
			if (boardId == null) {
				if (other.boardId != null)
					return false;
			} else if (!boardId.equals(other.boardId))
				return false;
			if (id == null) {
				if (other.id != null)
					return false;
			} else if (!id.equals(other.id))
				return false;
			return true;
		}
	}
	
	@Column(name = "BORD_ID")
	String boardId;
	
	public BoardArticle() {}
	
	public BoardArticle(String boardId, String id) {
		this.boardId = boardId;
		this.setId(id);
	}

	public String getBoardId() {
		return boardId;
	}

	public void setBoardId(String boardId) {
		this.boardId = boardId;
	}

}
