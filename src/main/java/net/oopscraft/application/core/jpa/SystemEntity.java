package net.oopscraft.application.core.jpa;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class SystemEntity {
	
	@Column(name="SYS_DATA_YN")
	String systemDataYn;
	
	@Column(name="SYS_INST_DTTM")
	Date systemInsertDate;
	
	@Column(name="SYS_INST_USER_ID")
	String systemInsertUserId;
	
	@Column(name="SYS_UPDT_DTTM")
	Date systemUpdateDate;
	
	@Column(name="SYS_UPDT_USER_ID")
	String systemUpdateUserId;

	public String getSystemDataYn() {
		return systemDataYn;
	}

	public void setSystemDataYn(String systemDataYn) {
		this.systemDataYn = systemDataYn;
	}

	public Date getSystemInsertDate() {
		return systemInsertDate;
	}

	public void setSystemInsertDate(Date systemInsertDate) {
		this.systemInsertDate = systemInsertDate;
	}

	public String getSystemInsertUserId() {
		return systemInsertUserId;
	}

	public void setSystemInsertUserId(String systemInsertUserId) {
		this.systemInsertUserId = systemInsertUserId;
	}

	public Date getSystemUpdateDate() {
		return systemUpdateDate;
	}

	public void setSystemUpdateDate(Date systemUpdateDate) {
		this.systemUpdateDate = systemUpdateDate;
	}

	public String getSystemUpdateUserId() {
		return systemUpdateUserId;
	}

	public void setSystemUpdateUserId(String systemUpdateUserId) {
		this.systemUpdateUserId = systemUpdateUserId;
	}
	
	
	
}
