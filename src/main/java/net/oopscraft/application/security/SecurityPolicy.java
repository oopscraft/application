package net.oopscraft.application.security;

public enum SecurityPolicy {

	ANONYMOUS("Anonymous"), 
	AUTHENTICATED("Only Authenticated"), 
	AUTHORIZED("Only Authorized");
	
	String name;
	
	SecurityPolicy(String name) {
		this.name = name;
	}
	
}
