package net.oopscraft.application.page;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import net.oopscraft.application.core.jpa.SystemEntity;
import net.oopscraft.application.message.MessageI18n;

@Entity
@Table(name = "APP_PAGE_I18N")
@IdClass(MessageI18n.Pk.class)
public class PageI18n extends SystemEntity {

	public static class Pk implements Serializable {

		private static final long serialVersionUID = -5084946285734554813L;
		String id;
		String language;
		
		public Pk() {}
		public Pk(String id, String language) {
			this.id = id;
			this.language = language;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getLanguage() {
			return language;
		}
		public void setLanguage(String language) {
			this.language = language;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((id == null) ? 0 : id.hashCode());
			result = prime * result + ((language == null) ? 0 : language.hashCode());
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
			if (id == null) {
				if (other.id != null)
					return false;
			} else if (!id.equals(other.id))
				return false;
			if (language == null) {
				if (other.language != null)
					return false;
			} else if (!language.equals(other.language))
				return false;
			return true;
		}
	}
	
	@Id
	@Column(name="PAGE_ID", length=32)
	String id;

	@Id
	@Column(name="PAGE_LANG", length=32)
	@NotNull
	String language;
	
	@Column(name="PAGE_CNTS", length=Integer.MAX_VALUE)
	@Lob
	String contents;

	public PageI18n() {}
	
	public PageI18n(String id, String language) {
		this.id = id;
		this.language = language;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}
	
}
