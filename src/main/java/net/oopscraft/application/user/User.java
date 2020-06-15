package net.oopscraft.application.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import net.oopscraft.application.core.jpa.SystemEntity;

@Entity
@Table(
	name = "APP_USER_INFO",
	uniqueConstraints={@UniqueConstraint(columnNames={"USER_EMAL"})},
	indexes = {
		@Index(columnList="SYS_INST_DATE"),
		@Index(columnList="USER_EMAL,SYS_INST_DATE"),
		@Index(columnList="USER_NAME,SYS_INST_DATE")
	}
)
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class User extends SystemEntity {
	
	@Id
	@Column(name = "USER_ID", length = 32)
	@JsonView(List.class)
	String id;
	
	@Column(name = "USER_EMAL")
	@NotNull
	@JsonView(List.class)
	String email;
	
	public enum Status {
		ACTIVE, SUSPENDED, CLOSED
	}
	@Column(name = "USER_STAT")
	@Enumerated(EnumType.STRING)
	@NotNull
	@JsonView(List.class)
	Status status;

	@Column(name = "USER_NAME", length = 1024)
	@NotNull
	@JsonView(List.class)
	String name;

	@Column(name = "USER_MOBL_CTRY")
	@JsonView(List.class)
	String mobileCountry;
	
	@Column(name = "USER_MOBL_NO")
	@JsonView(List.class)
	String mobileNumber;

	@JsonIgnore
	@Column(name = "USER_PASS", length = 255)
	String password;
	
	@Column(name = "USER_PHOT", length = Integer.MAX_VALUE)
	@Lob
	@Basic(fetch = FetchType.LAZY)
	String photo;
	
	@Column(name = "USER_THUM", length = Integer.MAX_VALUE)
	@Lob
	@JsonView(List.class)
	String thumbnail;

	@Column(name = "USER_PRFL", length = Integer.MAX_VALUE)
	@Lob
	String profile;
	
	@Column(name = "USER_LANG", length = 32)
	@JsonView(List.class)
	String language;

	@Column(name = "JOIN_DATE")
	@JsonView(List.class)
	Date joinDate;
	
	@Column(name = "CLOS_DATE")
	@JsonView(List.class)
	Date closeDate;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "APP_USER_GROP_MAP", 
		joinColumns = @JoinColumn(name = "USER_ID"), 
		foreignKey = @ForeignKey(name = "none"),
		inverseJoinColumns = @JoinColumn(name = "GROP_ID"), 
		inverseForeignKey = @ForeignKey(name = "none")
	)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	List<Group> groups = new ArrayList<Group>();

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "APP_USER_ROLE_MAP", 
		joinColumns = @JoinColumn(name = "USER_ID"), 
		foreignKey = @ForeignKey(name = "none"),
		inverseJoinColumns = @JoinColumn(name = "ROLE_ID"), 
		inverseForeignKey = @ForeignKey(name = "none")
	)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	List<Role> roles = new ArrayList<Role>();

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "APP_USER_AUTH_MAP", 
		joinColumns = @JoinColumn(name = "USER_ID"), 
		foreignKey = @ForeignKey(name = "none"),
		inverseJoinColumns = @JoinColumn(name = "AUTH_ID"), 
		inverseForeignKey = @ForeignKey(name = "none")
	)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	List<Authority> authorities = new ArrayList<Authority>();
	
	public User() { }
	
	public User(String id) {
		this.id = id;
	}
	
	/**
	 * Returns available authorities
	 * @return
	 */
	public List<Authority> getAvailableAuthorities() {
		List<Authority> availableAuthorities = new ArrayList<Authority>();
		
		// adds own authorities
		for(Authority authority : this.getAuthorities()) {
			availableAuthorities.add(authority);
		}
		
		// adds role's authorities
		for(Role role : this.getRoles()){
			for(Authority authority : role.getAuthorities()) {
				if(!availableAuthorities.stream().anyMatch(e -> e.getId().equals(authority.getId()))) {
					authority.setHolder(role.getName());
					availableAuthorities.add(authority);
				}
			}
		}
		
		// adds groups's available authorities
		for(Group group : this.getGroups()) {
			for(Authority authority : group.getAvailableAuthorities()) {
				if(!availableAuthorities.stream().anyMatch(e -> e.getId().equals(authority.getId()))) {
					authority.setHolder(group.getName());
					availableAuthorities.add(authority);
				}
			}
		}
		
		// return
		return availableAuthorities;
	}
	
	/**
	 * hasAuthority
	 */
	public boolean hasAuthority(String id) {
		for(Authority authority : getAvailableAuthorities()) {
			if(authority.getId().contentEquals(id)) {
				return true;
			}
		}
		return false;
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

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
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
