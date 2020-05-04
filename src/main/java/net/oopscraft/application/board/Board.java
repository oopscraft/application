package net.oopscraft.application.board;

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
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Formula;

import net.oopscraft.application.core.jpa.BooleanStringConverter;
import net.oopscraft.application.core.jpa.SystemEntity;
import net.oopscraft.application.security.SecurityPolicy;
import net.oopscraft.application.user.Authority;

@Entity
@Table(name = "APP_BORD_INFO")
public class Board extends SystemEntity {

	@Id
	@Column(name = "BORD_ID", length = 32)
	@NotNull
	String id;

	@Column(name = "BORD_NAME", length = 1024)
	@NotNull
	String name;
	
	@Column(name = "BORD_ICON", length = Integer.MAX_VALUE)
	String icon;
	
	@Column(name = "BORD_DESC", length = Integer.MAX_VALUE)
	@Lob
	String description;
	
	@Column(name = "BORD_SKIN", length = 1024)
	String skin;
	
	@Column(name = "BORD_ROWS")
	Integer rows;
	
	@Formula("(select count(*) from APP_ATCL_INFO a where a.BORD_ID = BORD_ID)")
	Long articleCount;

	@Column(name = "RPLY_USE_YN", length = 1)
	@Convert(converter=BooleanStringConverter.class)
	boolean replyUse = false;
	
	@Column(name = "FILE_USE_YN")
	@Convert(converter=BooleanStringConverter.class)
	boolean fileUse = false;
	
	@Column(name = "FILE_ALOW_CNT")
	Integer fileAllowCount;
	
	@Column(name = "FILE_ALOW_SIZE")
	Integer fileAllowSize;
	
	@Column(name = "CATE_USE_YN")
	@Convert(converter=BooleanStringConverter.class)
	boolean categoryUse = false;
	
	@OneToMany(
		fetch = FetchType.LAZY, 
		mappedBy = "boardId", 
		cascade = CascadeType.ALL, 
		orphanRemoval = true
	)
	@OrderBy("displayNo")
	List<BoardCategory> categories = new ArrayList<BoardCategory>();
	
	@Column(name = "ACES_PLCY")
	@Enumerated(EnumType.STRING)
	SecurityPolicy accessPolicy = SecurityPolicy.ANONYMOUS;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "APP_BORD_AUTH_ACES_MAP", 
		joinColumns = @JoinColumn(name = "BORD_ID"),
		foreignKey = @ForeignKey(name = "none"),
		inverseJoinColumns = @JoinColumn(name = "AUTH_ID"),
		inverseForeignKey = @ForeignKey(name = "none")
	)
	List<Authority> accessAuthorities = new ArrayList<Authority>();

	@Column(name = "READ_PLCY")
	@Enumerated(EnumType.STRING)
	SecurityPolicy readPolicy = SecurityPolicy.ANONYMOUS;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "APP_BORD_AUTH_READ_MAP", 
		joinColumns = @JoinColumn(name = "BORD_ID"),
		foreignKey = @ForeignKey(name = "none"),
		inverseJoinColumns = @JoinColumn(name = "AUTH_ID"),
		inverseForeignKey = @ForeignKey(name = "none")
	)
	List<Authority> readAuthorities = new ArrayList<Authority>();
	
	@Column(name = "WRIT_PLCY")
	@Enumerated(EnumType.STRING)
	SecurityPolicy writePolicy = SecurityPolicy.ANONYMOUS;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "APP_BORD_AUTH_WRIT_MAP", 
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

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
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

	public String getSkin() {
		return skin;
	}

	public void setSkin(String skin) {
		this.skin = skin;
	}

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	public Long getArticleCount() {
		return articleCount;
	}

	public void setArticleCount(Long articleCount) {
		this.articleCount = articleCount;
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

	public Integer getFileAllowCount() {
		return fileAllowCount;
	}

	public void setFileAllowCount(Integer fileAllowCount) {
		this.fileAllowCount = fileAllowCount;
	}

	public Integer getFileAllowSize() {
		return fileAllowSize;
	}

	public void setFileAllowSize(Integer fileAllowSize) {
		this.fileAllowSize = fileAllowSize;
	}

	public boolean isCategoryUse() {
		return categoryUse;
	}

	public void setCategoryUse(boolean categoryUse) {
		this.categoryUse = categoryUse;
	}

	public List<BoardCategory> getCategories() {
		return categories;
	}

	public void setCategories(List<BoardCategory> categories) {
		this.categories = categories;
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

	public void setReadPolicy(SecurityPolicy readPolicy) {
		this.readPolicy = readPolicy;
	}

	public List<Authority> getReadAuthorities() {
		return readAuthorities;
	}

	public void setReadAuthorities(List<Authority> readAuthorities) {
		this.readAuthorities = readAuthorities;
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

}
