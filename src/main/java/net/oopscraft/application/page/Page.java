package net.oopscraft.application.page;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonView;

import net.oopscraft.application.core.jpa.SystemEntity;
import net.oopscraft.application.security.SecurityPolicy;
import net.oopscraft.application.user.Authority;

@Entity
@Table(name="APP_PAGE_INFO")
public class Page extends SystemEntity {
	
	@Id
	@Column(name="PAGE_ID", length=32)
	@JsonView(List.class)
	String id;

	@Column(name="PAGE_NAME", length=4000)
	@NotNull
	@JsonView(List.class)
	String name;
	
	public enum Format { HTML, MARKDOWN }
	@Column(name = "PAGE_FMAT", length = 64)
	@Enumerated(EnumType.STRING)
	@JsonView(List.class)
	Format format;
	
	@Column(name="PAGE_DESC", length=Integer.MAX_VALUE)
	@Lob
	@JsonView(List.class)
	String description;
	
	@Column(name = "READ_PLCY")
	@Enumerated(EnumType.STRING)
	@JsonView(List.class)
	SecurityPolicy readPolicy = SecurityPolicy.ANONYMOUS;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "APP_PAGE_AUTH_READ_MAP", 
		joinColumns = @JoinColumn(name = "PAGE_ID"),
		foreignKey = @ForeignKey(name = "none"),
		inverseJoinColumns = @JoinColumn(name = "AUTH_ID"),
		inverseForeignKey = @ForeignKey(name = "none")
	)
	List<Authority> readAuthorities = new ArrayList<Authority>();
	
	@Column(name = "EDIT_PLCY")
	@Enumerated(EnumType.STRING)
	@JsonView(List.class)
	SecurityPolicy editPolicy = SecurityPolicy.AUTHORIZED;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "APP_PAGE_AUTH_EDIT_MAP", 
		joinColumns = @JoinColumn(name = "PAGE_ID"),
		foreignKey = @ForeignKey(name = "none"),
		inverseJoinColumns = @JoinColumn(name = "AUTH_ID"),
		inverseForeignKey = @ForeignKey(name = "none")
	)
	@JsonView(List.class)
	List<Authority> editAuthorities = new ArrayList<Authority>();
	
	@OneToMany(
		fetch = FetchType.LAZY, 
		mappedBy = "id", 
		cascade = CascadeType.ALL, 
		orphanRemoval = true
	)
	@OrderBy("language")
	List<PageDetail> details = new ArrayList<PageDetail>();
	
	public Page() {}
	
	public Page(String id) {
		this.id=id;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Format getFormat() {
		return format;
	}

	public void setFormat(Format format) {
		this.format = format;
	}

	public SecurityPolicy getReadPolicy() {
		return readPolicy;
	}

	public void setReadPolicy(SecurityPolicy readPolicy) {
		this.readPolicy = readPolicy;
	}

	public List<Authority> getReadAuthorities() {
		return readAuthorities;
	}

	public void setReadAuthorities(List<Authority> readAuthorities) {
		this.readAuthorities = readAuthorities;
	}

	public SecurityPolicy getEditPolicy() {
		return editPolicy;
	}

	public void setEditPolicy(SecurityPolicy editPolicy) {
		this.editPolicy = editPolicy;
	}

	public List<Authority> getEditAuthorities() {
		return editAuthorities;
	}

	public void setEditAuthorities(List<Authority> editAuthorities) {
		this.editAuthorities = editAuthorities;
	}

	public List<PageDetail> getDetails() {
		return details;
	}

	public void setDetails(List<PageDetail> details) {
		this.details = details;
	}

}
