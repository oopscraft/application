package net.oopscraft.application.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.oopscraft.application.user.entity.Authority;
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
		
		// adds roles
		for(Role role : user.getAvailableRoles()) {
			authorities.add(new GrantedAuthority(role));
		}
		
		// adds authorities
		for(Authority authority : user.getAvailableAuthorities()) {
			authorities.add(new GrantedAuthority(authority));
		}
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
