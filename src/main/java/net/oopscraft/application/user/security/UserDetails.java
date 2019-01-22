package net.oopscraft.application.user.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;

import net.oopscraft.application.core.TextTable;
import net.oopscraft.application.user.Authority;
import net.oopscraft.application.user.Group;
import net.oopscraft.application.user.Role;
import net.oopscraft.application.user.User;

public class UserDetails implements org.springframework.security.core.userdetails.UserDetails {

	private static final long serialVersionUID = 4282816224569702221L;
	private static final Logger LOGGER = LoggerFactory.getLogger(UserDetails.class);
	
	User user;
	List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
	
	public UserDetails(User user) {
		this.user = user;
	}
	
	/**
	 * loadAuthorities
	 */
	public void loadAuthorities() {
		
		// clear
		authorities.clear();
		
		// Adds user authorities
		for(Authority authority : user.getAuthorities()) {
			addAuthority(new net.oopscraft.application.user.security.GrantedAuthority(authority));
		}
		
		// Adds user roles
		for(Role role : user.getRoles()) {
			addAuthority(new net.oopscraft.application.user.security.GrantedAuthority(role));
			for(Authority authority : role.getAuthorities()) {
				addAuthority(new net.oopscraft.application.user.security.GrantedAuthority(authority));
			}
		}
		
		// load authorities from group
		List<Group> groups = user.getGroups();
		for(Group group : groups) {
			// Adds group roles
			for(Role role : group.getRoles()) {
				this.authorities.add(new net.oopscraft.application.user.security.GrantedAuthority(role));
				for(Authority authority : role.getAuthorities()) {
					addAuthority(new net.oopscraft.application.user.security.GrantedAuthority(authority));
				}
			}
			// Adds group authorities
			for(Authority authority : group.getAuthorities()) {
				this.addAuthority(new net.oopscraft.application.user.security.GrantedAuthority(authority));
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
	
	public User getUser() {
		return user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}
	
	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getId();
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


}
