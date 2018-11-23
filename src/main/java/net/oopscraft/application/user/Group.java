package net.oopscraft.application.user;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "APP_GROUP_INFO")
public class Group {

	@Id
	@Column(name = "GROUP_ID")
	String id;

	@Column(name = "UPPER_GROUP_ID")
	String upperId;

	@Column(name = "GROUP_NAME")
	String name;

	@Column(name = "GROUP_DESC")
	String description;

	@Column(name = "DISP_SEQ")
	Integer displaySeq;

	//@Transient
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "UPPER_GROUP_ID")
	List<Group> childGroups = new ArrayList<Group>();

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "APP_GROUP_ROLE_MAP", joinColumns = @JoinColumn(name = "GROUP_ID"), inverseJoinColumns = @JoinColumn(name = "ROLE_ID"))
	List<Role> roles;

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "APP_GROUP_AUTH_MAP", joinColumns = @JoinColumn(name = "GROUP_ID"), inverseJoinColumns = @JoinColumn(name = "AUTH_ID"))
	List<Authority> authorities;

	public void setChildGroups(List<Group> childGroups) {
		this.childGroups = childGroups;
	}

	public void addChildGroup(Group group) {
		childGroups.add(group);
	}

	public List<Group> getChildGroups() {
		return childGroups;
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

	public Integer getDisplaySeq() {
		return displaySeq;
	}

	public void setDisplaySeq(Integer displaySeq) {
		this.displaySeq = displaySeq;
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
