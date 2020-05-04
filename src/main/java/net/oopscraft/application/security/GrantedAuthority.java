package net.oopscraft.application.security;

import net.oopscraft.application.user.Authority;

public class GrantedAuthority implements org.springframework.security.core.GrantedAuthority{

	private static final long serialVersionUID = -3886138225661206288L;
	
	String authority;
	
	GrantedAuthority(){}
	
	GrantedAuthority(Authority authority){
		this.authority = authority.getId();
	}
	
	public void setAuthority(String authority) {
		this.authority = authority;
	}

	@Override
	public String getAuthority() {
		return authority;
	}

}
