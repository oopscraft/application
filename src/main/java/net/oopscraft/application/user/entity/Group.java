package net.oopscraft.application.user.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import net.oopscraft.application.core.jpa.SystemEntity;
import net.oopscraft.application.core.jpa.SystemEntityListener;

@Entity
@Table(name = "APP_GROP_INFO")
@EntityListeners(SystemEntityListener.class)
public class Group extends SystemEntity {

	@Id
	@Column(name = "GROP_ID", length=32)
	String id;

	@Column(name = "UPER_GROP_ID", length=32)
	String upperId;

	@Column(name = "GROP_NAME")
	String name;
	
	@Column(name = "GROP_ICON", length=4000)
	String icon;

	@Column(name = "GROP_DESC", length=4000)
	String description;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "APP_GROP_ROLE_MAP", 
		joinColumns = @JoinColumn(name = "GROP_ID"), 
		foreignKey = @ForeignKey(name = "none"),
		inverseJoinColumns = @JoinColumn(name = "ROLE_ID"), 
		inverseForeignKey = @ForeignKey(name = "none")
	)
	List<Role> roles;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "APP_GROP_AUTH_MAP",
		joinColumns = @JoinColumn(name = "GROP_ID"),
		foreignKey = @ForeignKey(name = "none"),
		inverseJoinColumns = @JoinColumn(name = "AUTH_ID"),
		inverseForeignKey = @ForeignKey(name = "none")
	)
	List<Authority> authorities;

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

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public List<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(List<Authority> authorities) {
		this.authorities = authorities;
	}

}
