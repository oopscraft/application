package net.oopscraft.application.board.entity;

import java.io.File;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "APP_BORD_ATCL_FILE_INFO")
@IdClass(BoardArticleFile.Pk.class)
public class BoardArticleFile {

	public static class Pk implements Serializable {
		private static final long serialVersionUID = 4612920412687440925L;
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
	@Column(name = "FILE_ID")
	String id;
	
	@Column(name = "FILE_NAME")
	String name;
	
	@Column(name = "FILE_TYPE")
	String type;
	
	@Column(name = "FILE_SIZE")
	long size;
	
	/**
	 * Returns temporary file.
	 * @return
	 */
	public File getTemporaryFile() {
		return new File(".temp" + File.separator + "board" + File.separator + id);
	}
	
	/**
	 * Returns real file.
	 * @return
	 */
	public File getRealFile() {
		return new File("data" + File.separator + "board" + File.separator + id);
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

}
