package net.oopscraft.application.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "APP_USER_STAT_CD")
public class UserStatusCd {

	@Id
	@Column(name = "USER_STAT_CD")
	String cd;
	
	@Column(name = "USER_STAT_NAME")
	String name;
	
	@Column(name = "USER_STAT_DESC")
	String description;
	
	@Column(name = "DISP_SEQ")
	String displaySeq;
	
	public String getCd() {
		return cd;
	}
	public void setCd(String cd) {
		this.cd = cd;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

}