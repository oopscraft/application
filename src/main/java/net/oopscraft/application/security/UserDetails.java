package net.oopscraft.application.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.oopscraft.application.user.entity.Authority;
import net.oopscraft.application.user.entity.Group;
import net.oopscraft.application.user.entity.Role;
import net.oopscraft.application.user.entity.User;

public class UserDetails implements org.springframework.security.core.userdetails.UserDetails {

	private static final long serialVersionUID = 4282816224569702221L;
	
	String username;
	List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
	String language;
	
	public UserDetails() {}
	
	public UserDetails(User user) {
		this.username = user.getId();
		this.language = user.getLanguage();
		
		// Adds user authorities
		for(Authority authority : user.getAuthorities()) {
			addAuthority(new GrantedAuthority(authority));
		}
		
		// Adds user roles
		for(Role role : user.getRoles()) {
			addAuthority(new GrantedAuthority(role));
			for(Authority authority : role.getAuthorities()) {
				addAuthority(new GrantedAuthority(authority));
			}
		}
		
		// load authorities from group
		List<Group> groups = user.getGroups();
		for(Group group : groups) {
			// Adds group roles
			for(Role role : group.getRoles()) {
				this.authorities.add(new GrantedAuthority(role));
				for(Authority authority : role.getAuthorities()) {
					addAuthority(new GrantedAuthority(authority));
				}
			}
			// Adds group authorities
			for(Authority authority : group.getAuthorities()) {
				this.addAuthority(new GrantedAuthority(authority));
			}
		}
	}
	
	/**
	 * Adds Granted Authority
	 * @param authority
	 */
	private void addAuthority(GrantedAuthority authority) {
		// checks duplicated authority
		boolean contains = false;
		for(GrantedAuthority grantedAuthority : this.authorities) {
			if(grantedAuthority.getAuthority().equals(authority.getAuthority())) {
				contains = true;
				break;
			}
		}
		// if not duplicated, add inputed authority
		if(contains == false) {
			this.authorities.add(authority);
		}
	}
	
	/**
	 * checks has authority
	 * @param authority
	 * @return
	 */
	public boolean hasAuthority(Authority authority) {
		for(GrantedAuthority element : this.authorities) {
			if(element.getAuthority().equals(authority.getId())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * checks has authorities
	 * @param authorities
	 * @return
	 */
	public boolean hasAuthority(List<Authority> authorities) {
		for(Authority authority : authorities) {
			if(hasAuthority(authority)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	@Override
	public String getUsername() {
		return username;
	}
	
	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}


}
