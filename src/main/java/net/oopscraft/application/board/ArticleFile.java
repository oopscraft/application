package net.oopscraft.application.board;

import java.io.File;
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

	/**
	 * ID Class
	 *
	 */
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
