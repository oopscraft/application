package net.oopscraft.application.user.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import net.oopscraft.application.core.jpa.SystemEntity;
import net.oopscraft.application.core.jpa.SystemEntityListener;

@Entity
@Table(name = "APP_USER_INFO")
@EntityListeners(SystemEntityListener.class)
public class User extends SystemEntity {
	
	@Id
	@Column(name = "USER_ID", length = 32)
	String id;
	
	@Column(name = "USER_EMAL")
	String email;

	@Column(name = "USER_MOBL_CTRY")
	String mobileCountry;
	
	@Column(name = "USER_MOBL_NO")
	String mobileNumber;

	@JsonIgnore
	@Column(name = "USER_PASS", length = 255)
	String password;

	public enum Status {
		ACTIVE, SUSPENDED, CLOSED
	}
	@Column(name = "USER_STAT")
	@Enumerated(EnumType.STRING)
	Status status;

	@Column(name = "USER_NAME", length = 1024)
	String name;
	
	@Column(name = "USER_NICK", length = 1024)
	String nickname;

	@Column(name = "USER_PHOT", length = Integer.MAX_VALUE)
	@Lob
	String photo;

	@Column(name = "USER_PRFL")
	@Lob
	String profile;
	
	@Column(name = "USER_LANG")
	String language;

	@Column(name = "JOIN_DATE")
	Date joinDate;
	
	@Column(name = "CLOS_DATE")
	Date closeDate;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "APP_USER_GROP_MAP", 
		joinColumns = @JoinColumn(name = "USER_ID"), 
		foreignKey = @ForeignKey(name = "none"),
		inverseJoinColumns = @JoinColumn(name = "GROP_ID"), 
		inverseForeignKey = @ForeignKey(name = "none")
	)
	List<Group> groups = new ArrayList<Group>();

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "APP_USER_ROLE_MAP", 
		joinColumns = @JoinColumn(name = "USER_ID"), 
		foreignKey = @ForeignKey(name = "none"),
		inverseJoinColumns = @JoinColumn(name = "ROLE_ID"), 
		inverseForeignKey = @ForeignKey(name = "none")
	)
	List<Role> roles = new ArrayList<Role>();

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "APP_USER_AUTH_MAP", 
		joinColumns = @JoinColumn(name = "USER_ID"), 
		foreignKey = @ForeignKey(name = "none"),
		inverseJoinColumns = @JoinColumn(name = "AUTH_ID"), 
		inverseForeignKey = @ForeignKey(name = "none")
	)
	List<Authority> authorities = new ArrayList<Authority>();
	
	public User() { }
	
	public User(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@JsonIgnore
	public String getPassword() {
		return password;
	}

	@JsonProperty
	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}
 
	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobileCountry() {
		return mobileCountry;
	}

	public void setMobileCountry(String mobileCountry) {
		this.mobileCountry = mobileCountry;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
	
	public Date getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(Date joinDate) {
		this.joinDate = joinDate;
	}

	public Date getCloseDate() {
		return closeDate;
	}

	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
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
	
	public void addAuthority(Authority authority) {
		this.authorities.add(authority);
	}

}
