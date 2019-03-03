package net.oopscraft.application.board;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;

@Entity
@Table(name = "APP_BORD_CATE_INFO")
@IdClass(BoardCategory.Pk.class)
public class BoardCategory {
	
	@EqualsAndHashCode
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
	}
	
	@Id
	@Column(name = "BORD_ID")
	String boardId;
	
	@Id
	@Column(name = "CATE_ID")
	String id;
	
	@Column(name = "CATE_NAME")
	String name;
	
	@Column(name = "DISP_SEQ")
	int displaySeq;

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

	public int getDisplaySeq() {
		return displaySeq;
	}

	public void setDisplaySeq(int displaySeq) {
		this.displaySeq = displaySeq;
	}

	
}
