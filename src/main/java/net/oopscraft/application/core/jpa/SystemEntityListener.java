package net.oopscraft.application.core.jpa;

import java.util.Date;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import net.oopscraft.application.security.entity.UserDetails;

public class SystemEntityListener {
	
	@PrePersist
	public void prePersist(SystemEntity systemEntity) throws Exception {
		systemEntity.setSystemEmbedded(false);
		systemEntity.setSystemInsertDate(new Date());
		systemEntity.setSystemInsertUserId(this.getUserId());
		
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
	private String getUserId() throws Exception {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authentication = securityContext.getAuthentication();
		if(authentication.getPrincipal() instanceof UserDetails) {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			return userDetails.getUsername();
		}else {
			return null;
		}
	}

}
