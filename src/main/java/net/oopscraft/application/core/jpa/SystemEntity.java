package net.oopscraft.application.core.jpa;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class SystemEntity {
	
	@Column(name="SYS_INST_DTTM")
	Date insertDate;
	
	@Column(name="SYS_INST_USER_ID")
	String insertUserId;
	
	@Column(name="SYS_UPDT_DTTM")
	Date updateDate;
	
	@Column(name="SYS_UPDT_USER_ID")
	String updateUserId;
	
	public Date getInsertDate() {
		return insertDate;
	}
	public void setInsertDate(Date insertDate) {
		this.insertDate = insertDate;
	}
	public String getInsertUserId() {
		return insertUserId;
	}
	public void setInsertUserId(String insertUserId) {
		this.insertUserId = insertUserId;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getUpdateUserId() {
		return updateUserId;
	}
	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}
	
}
