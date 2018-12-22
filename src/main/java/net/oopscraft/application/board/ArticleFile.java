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
		public Pk(String articleNo, String name) {
			this.articleNo = articleNo;
			this.name = name;
		}
		String articleNo;
		String name;
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof Pk) {
				Pk pk = (Pk)obj;
				if(this.getArticleNo().equals(pk.getArticleNo())
				&& this.getName() == pk.getName()
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
			return (articleNo + name).hashCode();
		}
		
		public String getArticleNo() {
			return articleNo;
		}
		public void setArticleNo(String articleNo) {
			this.articleNo = articleNo;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	}
	
	@Id
	@Column(name = "ATCL_NO")
	long articleNo;
	
	@Id
	@Column(name = "ATCL_FILE_NAME")
	String name;
	
	@Id
	@Column(name = "ATCL_FILE_SIZE")
	long size;

}
