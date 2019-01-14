package net.oopscraft.application.menu;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.SQLInsert;
import org.hibernate.annotations.WhereJoinTable;

import net.oopscraft.application.core.jpa.SystemEntity;
import net.oopscraft.application.core.jpa.SystemEntityListener;
import net.oopscraft.application.user.Authority;

@Entity
@Table(name = "APP_MENU_INFO")
@EntityListeners(SystemEntityListener.class)
public class Menu extends SystemEntity {

	@Id
	@Column(name = "MENU_ID")
	String id;

	@Column(name = "UPER_MENU_ID")
	String upperId;

	@Column(name = "MENU_NAME")
	String name;

	@Column(name = "MENU_ICON")
	String icon;
	
	@Column(name = "MENU_LINK")
	String link;

	@Column(name = "MENU_DESC")
	String description;

	@Column(name = "DISP_SEQ")
	Integer displaySeq;

	public enum Policy {
		ANONYMOUS, AUTHENTICATED, AUTHORIZED
	}

	@Column(name = "DISP_PLCY")
	@Enumerated(EnumType.STRING)
	Policy displayPolicy = Policy.ANONYMOUS;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "APP_MENU_PLCY_AUTH_MAP", joinColumns = @JoinColumn(name = "MENU_ID"), inverseJoinColumns = @JoinColumn(name = "AUTH_ID"))
	@WhereJoinTable(clause = "PLCY_TYPE ='DISP_PLCY'")
	@SQLInsert(sql = "INSERT INTO APP_MENU_PLCY_AUTH_MAP (MENU_ID, PLCY_TYPE, AUTH_ID) VALUES (?, 'DISP_PLCY', ?)") 
	List<Authority> displayAuthorities;
	
	@Transient
	List<Menu> childMenus;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUpperId() {
		return upperId;
	}

	public void setUpperId(String upperId) {
		this.upperId = upperId;
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

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getDisplaySeq() {
		return displaySeq;
	}

	public void setDisplaySeq(Integer displaySeq) {
		this.displaySeq = displaySeq;
	}

	public Policy getDisplayPolicy() {
		return displayPolicy;
	}

	public void setDisplayPolicy(Policy displayPolicy) {
		this.displayPolicy = displayPolicy;
	}

	public List<Authority> getDisplayAuthorities() {
		return displayAuthorities;
	}

	public void setDisplayAuthorities(List<Authority> displayAuthorities) {
		this.displayAuthorities = displayAuthorities;
	}

	public List<Menu> getChildMenus() {
		return childMenus;
	}

	public void setChildMenus(List<Menu> childMenus) {
		this.childMenus = childMenus;
	}

}
