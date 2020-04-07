package net.oopscraft.application.board.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "APP_BORD_ATCL")
@IdClass(BoardArticle.Pk.class)
public class BoardArticle {
	
	/**
	 * ClassItem.Pk
	 */
	public static class Pk implements Serializable {
		private static final long serialVersionUID = -1395524870126554667L;
		String boardId;
		String id;
		public Pk() {}
		public Pk(String boardId, String id) {
			this.boardId = boardId;
			this.id = id;
		}
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

	@Id
	@Column(name = "BORD_ID", length = 32)
	String boardId;
	
	@Id
	@Column(name = "ATCL_ID", length = 32)
	String id;
	
	@Column(name = "CATE_ID", length = 32)
	String categoryId;
	
	@Column(name = "ATCL_TITL", length = 4000)
	String title;

	@Column(name = "ATCL_CNTS", length = Integer.MAX_VALUE)
	@Lob
	String contents;

	@Column(name = "ATCL_ATHR", length = 1024)
	String author;

	@Column(name = "USER_ID", length = 32)
	String userId;
	
	@Column(name = "WRIT_DATE")
	Date writeDate;
	
	@Column(name = "MDFY_DATE")
	Date modifyDate;
	
	public BoardArticle() {}
	
	public BoardArticle(String boardId, String articleId) {
		this.boardId = boardId;
		this.id = articleId;
	}

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

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
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
