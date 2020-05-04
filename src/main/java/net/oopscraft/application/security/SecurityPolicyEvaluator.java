package net.oopscraft.application.security;

import java.util.List;

import org.springframework.security.access.AccessDeniedException;

import net.oopscraft.application.user.Authority;

public class SecurityPolicyEvaluator {

	/**
	 * hasPolicyAuthority
	 * @param securityPolicy
	 * @param policyAuthorities
	 * @param userDetails
	 * @return
	 */
	public static boolean hasPolicyAuthority(SecurityPolicy securityPolicy, List<Authority> policyAuthorities, UserDetails userDetails) {
		if(securityPolicy == SecurityPolicy.AUTHORIZED) {
			if(userDetails == null) {
				return false;
			}
			for(Authority authority : policyAuthorities) {
				if(userDetails.hasAuthority(authority.getId())) {	
					return true;
				}
			}
			return false;
		}else if(securityPolicy == SecurityPolicy.AUTHENTICATED) {
			if(userDetails != null) {
				return true;
			}
			return false;
		}
		return true;
	}
	
	/**
	 * checkPolicyAuthority 
	 * @param securityPolicy
	 * @param policyAuthorities
	 * @param userDetails
	 */
	public static void checkPolicyAuthority(SecurityPolicy securityPolicy, List<Authority> policyAuthorities, UserDetails userDetails) {
		if(!hasPolicyAuthority(securityPolicy, policyAuthorities, userDetails)) {
			throw new AccessDeniedException("ACCESS_DENIED");
		}
	}

}
