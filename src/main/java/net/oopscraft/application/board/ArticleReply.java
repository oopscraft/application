package net.oopscraft.application.board;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "APP_ATCL_RPLY_INFO")
@IdClass(ArticleReply.Pk.class)
public class ArticleReply {
	
	public static class Pk implements Serializable {
		private static final long serialVersionUID = 3127781407229494383L;
		public Pk() {}
		public Pk(String articleId, String id) {
			this.articleId = articleId;
			this.id = id;
		}
		String articleId;
		String id;
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Pk) {
				Pk pk = (Pk)obj;
				if(this.getArticleId().equals(pk.getArticleId())
				&& this.getId().equals(pk.getId())
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
			return (articleId + id).hashCode();
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
	}
	
	@Id
	@Column(name = "ATCL_ID")
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
