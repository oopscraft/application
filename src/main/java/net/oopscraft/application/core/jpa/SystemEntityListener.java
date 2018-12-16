package net.oopscraft.application.core.jpa;

import java.util.Date;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public class SystemEntityListener {
	
	@PrePersist
	public void prePersist(SystemEntity systemEntity) {
		systemEntity.setSystemDataYn("N");
		systemEntity.setSystemInsertDate(new Date());
	}
	
	@PreUpdate
	public void preUpdate(SystemEntity systemEntity) {
		systemEntity.setSystemUpdateDate(new Date());
	}
}
