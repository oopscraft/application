package net.oopscraft.application.board;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import javax.persistence.SecondaryTables;
import javax.persistence.Table;

import net.oopscraft.application.board.BoardArticle.Pk;

@Entity
@Table(name = "APP_ATCL_INFO")
@SecondaryTables({
	@SecondaryTable(name="APP_ATCL_CNTS_INFO", pkJoinColumns={
		@PrimaryKeyJoinColumn(name="ATCL_NO", referencedColumnName="ATCL_NO") 
	})
})
public class BoardArticle {
	
	public static class Pk implements Serializable {
		private static final long serialVersionUID = 3127781407229494383L;
		public Pk() {}
		public Pk(String boardId, long no) {
			this.boardId = boardId;
			this.no = no;
		}
		String boardId;
		long no;
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Pk) {
				Pk pk = (Pk)obj;
				if(this.getBoardId().equals(pk.getBoardId())
				&& this.getNo() == pk.getNo()
				) {
					return true;
				}else {
					return false;
				}
			}else {
				return false;
			}
		}
		
		@Override
		public int hashCode() {
			return (boardId + Long.toString(no)).hashCode();
		}
		public String getBoardId() {
			return boardId;
		}
		public void setBoardId(String boardId) {
			this.boardId = boardId;
		}
		public long getNo() {
			return no;
		}
		public void setNo(long no) {
			this.no = no;
		}
	}

	@Id
	@Column(name = "BORD_ID")
	String boardId;
	
	@Id
	@Column(name = "ATCL_NO")
	long no;
	
	@Column(name = "CATE_ID")
	String categoryId;
	
	@Column(name = "ATCL_TITL")
	String title;
	
	@Column(name = "ATCL_USER_ID")
	String userId;
	
	@Column(name = "ATCL_RGST_DTTM")
	Date registDate;
	
	@Column(name = "ATCL_MDFY_DTTM")
	Date modifyDate;
	
	@Column(table="APP_ATCL_CNTS_INFO", name="ATCL_CNTS")
	String contents;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "articleNo", cascade = CascadeType.ALL)
	List<BoardArticleFile> files = new ArrayList<BoardArticleFile>();
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "articleNo", cascade = CascadeType.ALL)
	@OrderBy("no,level")
	List<BoardArticleReply> replies = new ArrayList<BoardArticleReply>();

	public long getNo() {
		return no;
	}

	public void setNo(long no) {
		this.no = no;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getRegistDate() {
		return registDate;
	}

	public void setRegistDate(Date registDate) {
		this.registDate = registDate;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	
	public List<BoardArticleFile> getFiles() {
		return files;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public void setFiles(List<BoardArticleFile> files) {
		this.files = files;
	}

	public List<BoardArticleReply> getReplies() {
		return replies;
	}

	public void setReplies(List<BoardArticleReply> replies) {
		this.replies = replies;
	}

}
