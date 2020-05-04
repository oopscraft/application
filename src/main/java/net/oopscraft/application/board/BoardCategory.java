package net.oopscraft.application.board;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import net.oopscraft.application.core.jpa.SystemEntity;

@Entity
@Table(name = "APP_BORD_CATE_INFO")
@IdClass(BoardCategory.Pk.class)
public class BoardCategory extends SystemEntity {
	
	/**
	 * BoardCategory.Pk
	 */
	public static class Pk implements Serializable {
		private static final long serialVersionUID = 3127781407229494383L;
		public Pk() {}
		public Pk(String boardId, String id) {
			this.boardId = boardId;
			this.id = id;
		}
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
	
	public BoardCategory() {}
	
	public BoardCategory(String boardId, String id) {
		this.boardId = id;
		this.id = id;
	}
	
	@Id
	@Column(name = "BORD_ID", length = 32)
	String boardId;
	
	@Id
	@Column(name = "CATE_ID", length = 32)
	String id;

	@Column(name = "CATE_NAME", length = 1024)
	String name;
	
	@Column(name = "DISP_NO")
	Integer displayNo;
	
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getDisplayNo() {
		return displayNo;
	}

	public void setDisplayNo(Integer displayNo) {
		this.displayNo = displayNo;
	}
	
}
