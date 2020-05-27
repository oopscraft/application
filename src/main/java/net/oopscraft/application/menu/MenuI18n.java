package net.oopscraft.application.menu;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "APP_MENU_I18N")
@IdClass(MenuI18n.Pk.class)
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MenuI18n {
	
	public static class Pk implements Serializable {
		private static final long serialVersionUID = 6361017922014637343L;
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
	@Column(name = "MENU_ID", length = 32)
	@NotNull
	String id;

	@Id
	@Column(name = "MENU_LANG", length = 32)
	@NotNull
	String language;
	
	@Column(name = "MENU_NAME", length = 1024)
	@NotNull
	String name;
	
	public MenuI18n() {}
	
	public MenuI18n(String id, String language) {
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
