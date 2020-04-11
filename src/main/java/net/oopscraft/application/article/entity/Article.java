package net.oopscraft.application.article.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import net.oopscraft.application.core.jpa.SystemEntity;

@Entity
@Table(name="APP_ATCL")
@Inheritance(
	strategy=InheritanceType.JOINED
)
public abstract class Article extends SystemEntity {

	@Id
	@Column(name="ATCL_ID", length=32)
	String id;
	
	@Column(name="ATCL_ATHR")
	String author;
	
	@Column(name="ATCL_TITL", length=4000)
	String title;

	@Column(name="ATCL_CNTS")
	@Lob
	String contents;
	
	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumns({
		@JoinColumn(name="ATCL_ID", foreignKey=@ForeignKey(ConstraintMode.NO_CONSTRAINT))
	})
	List<ArticleFile> files=new ArrayList<ArticleFile>();
	
	public Article() {}
	
	public Article(String id) {
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
	
	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}	
	
	public List<ArticleFile> getFiles() {
		return files;
	}

	public void setFiles(List<ArticleFile> files) {
		this.files = files;
	}
	
}
