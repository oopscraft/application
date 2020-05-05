package net.oopscraft.application.page;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Formula;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import net.oopscraft.application.board.Article.Format;
import net.oopscraft.application.core.jpa.BooleanStringConverter;
import net.oopscraft.application.core.jpa.SystemEntity;

@Entity
@Table(name="APP_PAGE_INFO")
public class Page extends SystemEntity {
	
	@Id
	@Column(name="PAGE_ID", length=32)
	@NotNull
	String id;
	
	public enum Format { HTML, MARKDOWN }
	@Column(name = "ATCL_FOMT", length = 64)
	@Enumerated(EnumType.STRING)
	Format format;
	
	@Column(name="PAGE_TITL", length=4000)
	@NotNull
	String title;
	
	@Column(name="PAGE_CNTS", length=Integer.MAX_VALUE)
	@Lob
	String contents;
	
	public Page() {}
	
	public Page(String id) {
		this.id=id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id=id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title=title;
	}
	
	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents=contents;
	}

}
