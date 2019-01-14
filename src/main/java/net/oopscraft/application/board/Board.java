package net.oopscraft.application.board;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.SQLInsert;
import org.hibernate.annotations.WhereJoinTable;

import net.oopscraft.application.user.Authority;


@Entity
@Table(name = "APP_BORD_INFO")
public class Board {

	@Id
	@Column(name = "BORD_ID")
	String id;
	
	@Column(name = "BORD_NAME")
	String name;
	
	@Column(name = "BORD_ICON")
	String icon;
	
	@Column(name = "LAYT_ID")
	String layoutId;
	
	@Column(name = "SKIN_ID")
	String skinId;
	
	public enum Policy {
		ANONYMOUS, AUTHENTICATED, AUTHORIZED
	}

	@Column(name = "ACES_PLCY")
	@Enumerated(EnumType.STRING)
	Policy accessPolicy = Policy.ANONYMOUS;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "APP_BORD_PLCY_AUTH_MAP", joinColumns = @JoinColumn(name = "BORD_ID"), inverseJoinColumns = @JoinColumn(name = "AUTH_ID"))
	@WhereJoinTable(clause = "PLCY_TYPE ='ACES_PLCY'")
	@SQLInsert(sql = "INSERT INTO APP_BORD_PLCY_AUTH_MAP (BORD_ID, PLCY_TYPE, AUTH_ID) VALUES (?, 'ACES_PLCY', ?)") 
	List<Authority> accessAuthorities = new ArrayList<Authority>();

	@Column(name = "READ_PLCY")
	@Enumerated(EnumType.STRING)
	Policy readPolicy = Policy.ANONYMOUS;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "APP_BORD_PLCY_AUTH_MAP", joinColumns = @JoinColumn(name = "BORD_ID"), inverseJoinColumns = @JoinColumn(name = "AUTH_ID"))
	@WhereJoinTable(clause = "PLCY_TYPE ='READ_PLCY'")
	@SQLInsert(sql = "INSERT INTO APP_BORD_PLCY_AUTH_MAP (BORD_ID, PLCY_TYPE, AUTH_ID) VALUES (?, 'READ_PLCY', ?)")
	List<Authority> readAuthorities = new ArrayList<Authority>();
	
	@Column(name = "WRIT_PLCY")
	@Enumerated(EnumType.STRING)
	Policy writePolicy = Policy.ANONYMOUS;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "APP_BORD_PLCY_AUTH_MAP", joinColumns = @JoinColumn(name = "BORD_ID"), inverseJoinColumns = @JoinColumn(name = "AUTH_ID"))
	@WhereJoinTable(clause = "PLCY_TYPE ='WRIT_PLCY'")
	@SQLInsert(sql = "INSERT INTO APP_BORD_PLCY_AUTH_MAP (BORD_ID, PLCY_TYPE, AUTH_ID) VALUES (?, 'WRIT_PLCY', ?)")
	List<Authority> writeAuthorities = new ArrayList<Authority>();
	
	@Column(name = "ROWS_PER_PAGE")
	int rowsPerPage = 10;
	
	@Column(name = "CATE_USE_YN")
	String categoryUseYn;
	
	@Column(name = "RPLY_USE_YN")
	String replyUseYn;
	
	@Column(name = "FILE_USE_YN")
	String fileUseYn;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "boardId", cascade = CascadeType.ALL)
	@OrderBy("displaySeq")
	List<BoardCategory> categories = new ArrayList<BoardCategory>();

	public enum ArticleSearchType {
		TITLE,
		TITLE_CONTENTS,
		USER
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

	public String getLayoutId() {
		return layoutId;
	}

	public void setLayoutId(String layoutId) {
		this.layoutId = layoutId;
	}

	public String getSkinId() {
		return skinId;
	}

	public void setSkinId(String skinId) {
		this.skinId = skinId;
	}

	public Policy getAccessPolicy() {
		return accessPolicy;
	}

	public void setAccessPolicy(Policy accessPolicy) {
		this.accessPolicy = accessPolicy;
	}

	public List<Authority> getAccessAuthorities() {
		return accessAuthorities;
	}

	public void setAccessAuthorities(List<Authority> accessAuthorities) {
		this.accessAuthorities = accessAuthorities;
	}

	public Policy getReadPolicy() {
		return readPolicy;
	}

	public List<Authority> getReadAuthorities() {
		return readAuthorities;
	}

	public void setReadAuthorities(List<Authority> readAuthorities) {
		this.readAuthorities = readAuthorities;
	}

	public void setReadPolicy(Policy readPolicy) {
		this.readPolicy = readPolicy;
	}

	public Policy getWritePolicy() {
		return writePolicy;
	}

	public void setWritePolicy(Policy writePolicy) {
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

	public String getCategoryUseYn() {
		return categoryUseYn;
	}

	public void setCategoryUseYn(String categoryUseYn) {
		this.categoryUseYn = categoryUseYn;
	}

	public String getReplyUseYn() {
		return replyUseYn;
	}

	public void setReplyUseYn(String replyUseYn) {
		this.replyUseYn = replyUseYn;
	}

	public String getFileUseYn() {
		return fileUseYn;
	}

	public void setFileUseYn(String fileUseYn) {
		this.fileUseYn = fileUseYn;
	}

	public List<BoardCategory> getCategories() {
		return categories;
	}

	public void setCategories(List<BoardCategory> categories) {
		this.categories = categories;
	}

}
