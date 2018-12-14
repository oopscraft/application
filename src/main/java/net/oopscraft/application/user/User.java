package net.oopscraft.application.user;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import net.oopscraft.application.core.jpa.SystemEntity;
import net.oopscraft.application.core.jpa.SystemEntityListener;

@Entity
@Table(name = "APP_USER_INFO")
@EntityListeners(SystemEntityListener.class)
public class User extends SystemEntity {

	@Id
	@Column(name = "USER_ID")
	String id;

	@Column(name = "USER_PWD")
	String password;
	
	@Column(name = "USER_EMAIL")
	String email;

	@Column(name = "USER_LOCALE")
	String locale;
	
	@Column(name = "USER_PHONE")
	String phone;

	@Column(name = "USER_NAME")
	String name;

	@Column(name = "USER_STAT_CD")
	String statusCode;

	@Formula("(SELECT A.USER_STAT_NAME FROM APP_USER_STAT_CD A WHERE A.USER_STAT_CD = USER_STAT_CD)")
	String statusName;

	@Column(name = "USER_NICK")
	String nickname;
	
	@Column(name = "USER_AVAT")
	String avatar;

	@Column(name = "USER_SIGN")
	String signature;
	
	@Column(name = "USER_JOIN_DTTM")
	Date joinDate;
	
	@Column(name = "USER_CLOSE_DTTM")
	Date closeDate;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "APP_USER_GROUP_MAP", joinColumns = @JoinColumn(name = "USER_ID"), inverseJoinColumns = @JoinColumn(name = "GROUP_ID"))
	List<Group> groups;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "APP_USER_ROLE_MAP", joinColumns = @JoinColumn(name = "USER_ID"), inverseJoinColumns = @JoinColumn(name = "ROLE_ID"))
	List<Role> roles;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "APP_USER_AUTH_MAP", joinColumns = @JoinColumn(name = "USER_ID"), inverseJoinColumns = @JoinColumn(name = "AUTH_ID"))
	List<Authority> authorities;

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

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
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

}
