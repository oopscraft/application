package net.oopscraft.application.board.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
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

import org.hibernate.annotations.Formula;

import net.oopscraft.application.core.jpa.BooleanStringConverter;
import net.oopscraft.application.security.entity.SecurityPolicy;
import net.oopscraft.application.user.entity.Authority;

@Entity
@Table(name = "APP_BORD")
public class Board {

	@Id
	@Column(name = "BORD_ID", length = 32)
	String id;
	
	@Column(name = "BORD_ICON", length = Integer.MAX_VALUE)
	String icon;

	@Column(name = "BORD_NAME", length = 1024)
	String name;
	
	@Column(name = "BORD_DESC")
	@Lob
	String description;
	
	@Column(name = "BORD_SKIN")
	String skin;
	
	@Column(name = "ROWS_PER_PAGE")
	int rowsPerPage = 10;
	
	@Formula("(SELECT COUNT(*) FROM APP_BORD_ATCL A WHERE A.BORD_ID = BORD_ID)")
	long articleCount = 0;

	@Column(name = "RPLY_USE_YN", length = 1)
	@Convert(converter=BooleanStringConverter.class)
	boolean replyUse = false;
	
	@Column(name = "FILE_USE_YN")
	@Convert(converter=BooleanStringConverter.class)
	boolean fileUse = false;
	
	@Column(name = "CATE_USE_YN")
	@Convert(converter=BooleanStringConverter.class)
	boolean categoryUse = false;
	
	@OneToMany(
		fetch = FetchType.LAZY, 
		mappedBy = "boardId", 
		cascade = CascadeType.ALL, 
		orphanRemoval = true
	)
	@OrderBy("sequence")
	List<BoardCategory> categories = new ArrayList<BoardCategory>();
	
	@Column(name = "ACES_PLCY")
	@Enumerated(EnumType.STRING)
	SecurityPolicy accessPolicy = SecurityPolicy.ANONYMOUS;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "APP_BORD_AUTH_ACES", 
		joinColumns = @JoinColumn(name = "BORD_ID"),
		foreignKey = @ForeignKey(name = "none"),
		inverseJoinColumns = @JoinColumn(name = "AUTH_ID"),
		inverseForeignKey = @ForeignKey(name = "none")
	)
	List<Authority> accessAuthorities = new ArrayList<Authority>();

	@Column(name = "PLCY_READ")
	@Enumerated(EnumType.STRING)
	SecurityPolicy readPolicy = SecurityPolicy.ANONYMOUS;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "APP_BORD_AUTH_READ", 
		joinColumns = @JoinColumn(name = "BORD_ID"),
		foreignKey = @ForeignKey(name = "none"),
		inverseJoinColumns = @JoinColumn(name = "AUTH_ID"),
		inverseForeignKey = @ForeignKey(name = "none")
	)
	List<Authority> readAuthorities = new ArrayList<Authority>();
	
	@Column(name = "PLCY_WRIT")
	@Enumerated(EnumType.STRING)
	SecurityPolicy writePolicy = SecurityPolicy.ANONYMOUS;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "APP_BORD_AUTH_WRIT", 
		joinColumns = @JoinColumn(name = "BORD_ID"),
		foreignKey = @ForeignKey(name = "none"),
		inverseJoinColumns = @JoinColumn(name = "AUTH_ID"),
		inverseForeignKey = @ForeignKey(name = "none")
	)
	List<Authority> writeAuthorities = new ArrayList<Authority>();

	public Board() {}
	
	public Board(String id) {
		this.id = id;
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

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSkin() {
		return skin;
	}

	public void setSkin(String skin) {
		this.skin = skin;
	}
	
	public long getArticleCount() {
		return articleCount;
	}

	public void setArticleCount(long articleCount) {
		this.articleCount = articleCount;
	}

	public SecurityPolicy getAccessPolicy() {
		return accessPolicy;
	}

	public void setAccessPolicy(SecurityPolicy accessPolicy) {
		this.accessPolicy = accessPolicy;
	}

	public List<Authority> getAccessAuthorities() {
		return accessAuthorities;
	}

	public void setAccessAuthorities(List<Authority> accessAuthorities) {
		this.accessAuthorities = accessAuthorities;
	}

	public SecurityPolicy getReadPolicy() {
		return readPolicy;
	}

	public List<Authority> getReadAuthorities() {
		return readAuthorities;
	}

	public void setReadAuthorities(List<Authority> readAuthorities) {
		this.readAuthorities = readAuthorities;
	}

	public void setReadPolicy(SecurityPolicy readPolicy) {
		this.readPolicy = readPolicy;
	}

	public SecurityPolicy getWritePolicy() {
		return writePolicy;
	}

	public void setWritePolicy(SecurityPolicy writePolicy) {
		this.writePolicy = writePolicy;
	}
	
	public List<Authority> getWriteAuthorities() {
		return writeAuthorities;
	}

	public void setWriteAuthorities(List<Authority> writeAuthorities) {
		this.writeAuthorities = writeAuthorities;
	}

	public int getRowsPerPage() {
		return rowsPerPage;
	}

	public void setRowsPerPage(int rowsPerPage) {
		this.rowsPerPage = rowsPerPage;
	}

	public boolean isCategoryUse() {
		return categoryUse;
	}

	public void setCategoryUse(boolean categoryUse) {
		this.categoryUse = categoryUse;
	}

	public boolean isReplyUse() {
		return replyUse;
	}

	public void setReplyUse(boolean replyUse) {
		this.replyUse = replyUse;
	}

	public boolean isFileUse() {
		return fileUse;
	}

	public void setFileUse(boolean fileUse) {
		this.fileUse = fileUse;
	}

	public List<BoardCategory> getCategories() {
		return categories;
	}

	public void setCategories(List<BoardCategory> categories) {
		this.categories = categories;
	}

}
