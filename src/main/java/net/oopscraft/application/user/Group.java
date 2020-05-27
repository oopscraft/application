package net.oopscraft.application.user;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import net.oopscraft.application.core.jpa.SystemEntity;

@Entity
@Table(name = "APP_GROP_INFO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Group extends SystemEntity {

	@Id
	@Column(name = "GROP_ID", length = 32)
	String id;

	@Column(name = "GROP_NAME", length = 1024)
	@NotNull
	String name;

	@Column(name = "UPER_GROP_ID", length = 32)
	String upperId;
	
	@Column(name = "GROP_ICON", length = Integer.MAX_VALUE)
	@Lob
	String icon;

	@Column(name = "GROP_DESC", length = Integer.MAX_VALUE)
	@Lob
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
	
	public Group() { }
	
	public Group(String id) {
		this.id = id;
	}
	
	/**
	 * Returns holding authorities
	 * @return
	 */
	public List<Authority> getAvailableAuthorities() {
		List<Authority> availableAuthorities = new ArrayList<Authority>();
		
		// adds authorities
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
		
		// return
		return availableAuthorities;
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
