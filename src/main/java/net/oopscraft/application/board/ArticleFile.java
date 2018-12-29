package net.oopscraft.application.board;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "APP_ATCL_FILE_INFO")
@IdClass(ArticleFile.Pk.class)
public class ArticleFile {
	
	public static class Pk implements Serializable {
		private static final long serialVersionUID = 3127781407229494383L;
		public Pk() {}
		public Pk(long articleNo, String id) {
			this.articleNo = articleNo;
			this.id = id;
		}
		long articleNo;
		String id;
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Pk) {
				Pk pk = (Pk)obj;
				if(this.getArticleNo() == pk.getArticleNo()
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
			return (Long.toString(articleNo) + id).hashCode();
		}
		
		public long getArticleNo() {
			return articleNo;
		}
		public void setArticleNo(long articleNo) {
			this.articleNo = articleNo;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
	}
	
	@Id
	@Column(name = "ATCL_NO")
	long articleNo;
	
	@Id
	@Column(name = "FILE_ID")
	String id;
	
	@Column(name = "FILE_NAME")
	String name;
	
	@Column(name = "FILE_TYPE")
	String type;
	
	@Column(name = "FILE_SIZE")
	long size;

}
