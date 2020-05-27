package net.oopscraft.application.security;

import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import net.oopscraft.application.user.Authority;

public class SecurityEvaluator {
	
	private static UserDetails getUserDetails() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		if(securityContext == null) {
			return null;
		}
		Authentication authentication = securityContext.getAuthentication();
		if(authentication == null) {
			return null;
		}
		if(authentication.getPrincipal() instanceof UserDetails) {
			return (UserDetails) authentication.getPrincipal();
		}
		return null;
	}

	/**
	 * hasPolicyAuthority
	 * @param securityPolicy
	 * @param policyAuthorities
	 * @param userDetails
	 * @return
	 */
	public static boolean hasPolicyAuthority(SecurityPolicy securityPolicy, List<Authority> policyAuthorities) {
		UserDetails userDetails = getUserDetails();
		if(securityPolicy == SecurityPolicy.ANONYMOUS) {
			return true;
		}else if(securityPolicy == SecurityPolicy.AUTHENTICATED) {
			if(userDetails != null) {
				return true;
			}
		}else if(securityPolicy == SecurityPolicy.AUTHORIZED) {
			if(userDetails == null) {
				return false;
			}
			for(Authority policyAuthority : policyAuthorities) {
				if(userDetails.getUser().hasAuthority(policyAuthority.getId())) {
					return true;
				}
			}
		}
		
		// returns default false.
		return false;
	}
	
	/**
	 * checkPolicyAuthority 
	 * @param securityPolicy
	 * @param policyAuthorities
	 * @param userDetails
	 */
	public static void checkPolicyAuthority(SecurityPolicy securityPolicy, List<Authority> policyAuthorities) {
		if(!hasPolicyAuthority(securityPolicy, policyAuthorities)) {
			throw new AccessDeniedException("ACCESS_DENIED");
		}
	}

}
