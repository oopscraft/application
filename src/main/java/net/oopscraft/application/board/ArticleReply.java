package net.oopscraft.application.board;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity
@Table(name = "APP_ATCL_RPLY_INFO")
@IdClass(ArticleReply.Pk.class)
public class ArticleReply {
	
	public static class Pk implements Serializable {
		private static final long serialVersionUID = 3127781407229494383L;
		public Pk() {}
		public Pk(long articleNo, long no) {
			this.articleNo = articleNo;
			this.no = no;
		}
		long articleNo;
		long no;
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Pk) {
				Pk pk = (Pk)obj;
				if(this.getArticleNo() == pk.getArticleNo()
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
			return (Long.toString(articleNo) + Long.toString(no)).hashCode();
		}
		
		public long getArticleNo() {
			return articleNo;
		}
		public void setArticleNo(long articleNo) {
			this.articleNo = articleNo;
		}
		public long getNo() {
			return no;
		}
		public void setNo(long no) {
			this.no = no;
		}
	}
	
	@Id
	@Column(name = "ATCL_NO")
	long articleNo;
	
	@Id
	@Column(name = "RPLY_NO")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "hibernate_sequence")
	@TableGenerator(name = "hibernate_sequence", allocationSize = 1)
	long no;
	
	@Column(name = "UPER_RPLY_NO")
	long upperNo;
	
	@Column(name = "RPLY_SEQ")
	int sequence;
	
	@Column(name = "RPLY_LEVL")
	String level;
	
	@Column(name = "RPLY_CNTS")
	String contents;
	
	public long getArticleNo() {
		return articleNo;
	}

	public void setArticleNo(long articleNo) {
		this.articleNo = articleNo;
	}

	public long getNo() {
		return no;
	}

	public void setNo(long no) {
		this.no = no;
	}

	public long getUpperNo() {
		return upperNo;
	}

	public void setUpperNo(long upperNo) {
		this.upperNo = upperNo;
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
