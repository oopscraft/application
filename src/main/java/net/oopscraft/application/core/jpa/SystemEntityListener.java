package net.oopscraft.application.core.jpa;

import java.util.Date;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import net.oopscraft.application.security.UserDetails;

public class SystemEntityListener {
	
	@PrePersist
	public void prePersist(SystemEntity systemEntity) throws Exception {
		systemEntity.setSystemEmbedded(false);
		systemEntity.setSystemInsertDate(new Date());
		systemEntity.setSystemInsertUserId(getUserId());
		
	}
	
	@PreUpdate
	public void preUpdate(SystemEntity systemEntity) throws Exception {
		systemEntity.setSystemUpdateDate(new Date());
		systemEntity.setSystemUpdateUserId(getUserId());
	}
	
	/*
	 * Return login user id
	 * @return
	 * @throws Exception
	 */
	private static final String getUserId() throws Exception {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		if(securityContext == null) {
			return null;
		}
		Authentication authentication = securityContext.getAuthentication();
		if(authentication == null) {
			return null;
		}
		if(authentication.getPrincipal() instanceof UserDetails) {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			return userDetails.getUsername();
		}else {
			return null;
		}
	}

}
