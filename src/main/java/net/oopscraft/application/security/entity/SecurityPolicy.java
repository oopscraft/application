package net.oopscraft.application.security.entity;

public enum SecurityPolicy {

	ANONYMOUS("Anonymous"), 
	AUTHENTICATED("Only Authenticated"), 
	AUTHORIZED("Only Authorized");
	
	String name;
	
	SecurityPolicy(String name) {
		this.name = name;
	}
	
}
