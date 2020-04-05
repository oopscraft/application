package net.oopscraft.application.board.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "APP_BORD_ATCL_RPLY_INFO")
@IdClass(BoardArticleReply.Pk.class)
public class BoardArticleReply {
	
	/**
	 * ArticleReply.Pk
	 */
	public static class Pk implements Serializable {
		private static final long serialVersionUID = 1702004427021445728L;
		String boardId;
		String articleId;
		String id;
		public String getBoardId() {
			return boardId;
		}
		public void setBoardId(String boardId) {
			this.boardId = boardId;
		}
		public String getArticleId() {
			return articleId;
		}
		public void setArticleId(String articleId) {
			this.articleId = articleId;
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
			result = prime * result + ((articleId == null) ? 0 : articleId.hashCode());
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
			if (articleId == null) {
				if (other.articleId != null)
					return false;
			} else if (!articleId.equals(other.articleId))
				return false;
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
	String articleId;
	
	@Id
	@Column(name = "RPLY_ID")
	String id;
	
	@Column(name = "UPER_RPLY_ID")
	String upperId;
	
	@Column(name = "RPLY_SEQ")
	int sequence;
	
	@Column(name = "RPLY_LEVL")
	String level;
	
	@Column(name = "RPLY_CNTS")
	String contents;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUpperId() {
		return upperId;
	}

	public void setUpperId(String upperId) {
		this.upperId = upperId;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}
	
}
