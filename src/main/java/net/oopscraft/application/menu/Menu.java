package net.oopscraft.application.menu;

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

import net.oopscraft.application.core.jpa.SystemEntity;
import net.oopscraft.application.message.MessageDetail;
import net.oopscraft.application.security.SecurityPolicy;
import net.oopscraft.application.user.Authority;

@Entity
@Table(name = "APP_MENU_INFO")
public class Menu extends SystemEntity {
	
	@Id
	@Column(name = "MENU_ID", length = 32)
	String id;

	@Column(name = "MENU_NAME", length = 1024)
	@NotNull
	String name;
	
	@Column(name = "UPER_MENU_ID", length = 32)
	String upperId;
	
	@Column(name = "MENU_ICON", length = Integer.MAX_VALUE)
	@Lob
	String icon;

	@Column(name = "MENU_DESC", length = Integer.MAX_VALUE)
	@Lob
	String description;
	
	@Column(name = "DISP_NO")
	Integer displayNo;
	
	@Column(name = "LINK_URL", length = 1024)
	String linkUrl;

	public enum LinkTarget {
		SELF, BLANK
	}
	
	@Column(name = "LINK_TRGT")
	@Enumerated(EnumType.STRING)
	LinkTarget linkTarget;
	
	@Column(name = "DISP_PLCY")
	@Enumerated(EnumType.STRING)
	SecurityPolicy displayPolicy = SecurityPolicy.ANONYMOUS;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "APP_MENU_AUTH_DISP_MAP", 
		joinColumns = @JoinColumn(name = "MENU_ID"),
		foreignKey = @ForeignKey(name = "none"),
		inverseJoinColumns = @JoinColumn(name = "AUTH_ID"),
		inverseForeignKey = @ForeignKey(name = "none")
	)
	List<Authority> displayAuthorities = new ArrayList<Authority>();
	
	public Menu() {}
	
	public Menu(String id) {
		this.id = id;
	}

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getDisplayNo() {
		return displayNo;
	}

	public void setDisplayNo(Integer displayNo) {
		this.displayNo = displayNo;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public LinkTarget getLinkTarget() {
		return linkTarget;
	}

	public void setLinkTarget(LinkTarget linkTarget) {
		this.linkTarget = linkTarget;
	}

	public SecurityPolicy getDisplayPolicy() {
		return displayPolicy;
	}

	public void setDisplayPolicy(SecurityPolicy displayPolicy) {
		this.displayPolicy = displayPolicy;
	}

	public List<Authority> getDisplayAuthorities() {
		return displayAuthorities;
	}

	public void setDisplayAuthorities(List<Authority> displayAuthorities) {
		this.displayAuthorities = displayAuthorities;
	}

}
