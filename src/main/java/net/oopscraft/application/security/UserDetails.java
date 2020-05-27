package net.oopscraft.application.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.oopscraft.application.user.Authority;
import net.oopscraft.application.user.User;

public class UserDetails implements org.springframework.security.core.userdetails.UserDetails {

	private static final long serialVersionUID = 4282816224569702221L;
	
	User user;
	
	public UserDetails(User user) {
		this.user = user;
	}
	
	/**
	 * Returns user
	 * @return
	 */
	public User getUser() {
		return this.user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for(Authority authority : user.getAvailableAuthorities()) {
			authorities.add(new GrantedAuthority(authority));
		}
		return authorities;
	}
	
	@Override
	public String getUsername() {
		return user.getId();
	}
	
	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public boolean isAccountNonExpired() {
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
		if(this.user == null) {
			return null;
		}
		return user.getLanguage();
	}

}
