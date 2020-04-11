package net.oopscraft.application.core.jpa;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonProperty;

@MappedSuperclass
public class SystemEntity {
	
	@Column(name="SYS_EMBD_YN")
	@Convert(converter=BooleanStringConverter.class)
	@JsonProperty("systemEmbedded")
	boolean systemEmbedded;
	
	@Column(name="SYS_INST_DATE")
	@JsonProperty("systemInsertDate")
	Date systemInsertDate;
	
	@Column(name="SYS_INST_USER_ID")
	@JsonProperty("systemInsertUserId")
	String systemInsertUserId;
	
	@Column(name="SYS_UPDT_DATE")
	@JsonProperty("systemUpdateDate")
	Date systemUpdateDate;
	
	@Column(name="SYS_UPDT_USER_ID")
	@JsonProperty("systemUpdateUserId")
	String systemUpdateUserId;
	
	public boolean isSystemEmbedded() {
		return systemEmbedded;
	}
	
	public void setSystemEmbedded(boolean systemEmbedded) {
		this.systemEmbedded = systemEmbedded;
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
