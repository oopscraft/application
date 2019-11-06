package net.oopscraft.application.user.security;

import net.oopscraft.application.user.entity.Authority;
import net.oopscraft.application.user.entity.Role;

public class GrantedAuthority implements org.springframework.security.core.GrantedAuthority{

	private static final long serialVersionUID = -3886138225661206288L;
	private static final String ROLE_PREFIX = "ROLE_";
	
	String authority;
	
	GrantedAuthority(Role role){
		this.authority = ROLE_PREFIX + role.getId();
	}
	
	GrantedAuthority(Authority authority){
		this.authority = authority.getId();
	}

	@Override
	public String getAuthority() {
		return authority;
	}

}
