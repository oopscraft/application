package net.oopscraft.application.user;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import net.oopscraft.application.core.jpa.BooleanStringConverter;

@Entity
@Table(name = "APP_VERI_HIST")
public class Verification {
	
	@Id
	@Column(name = "VERI_ID", length = 32)
	@NotNull
	String id;
	
	@Column(name = "VERI_CODE", length = 16)
	@JsonIgnore
	String code;

	@Column(name = "USER_ID", length = 32)
	String userId;
	
	public enum IssueType { EMAIL, SMS }
	@Column(name = "ISSU_TYPE")
	@Enumerated(EnumType.STRING)
	@NotNull
	IssueType issueType;
	
	@Column(name = "ISSU_ADDR", length = 4000)
	String issueAddress;
	
	@Column(name = "ISSU_DATE")
	Date issueDate;
	
	@Column(name="USE_DATE")
	Date useDate;
	
	@Column(name="USE_YN")
	@Convert(converter=BooleanStringConverter.class)
	boolean use;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public IssueType getIssueType() {
		return issueType;
	}

	public void setIssueType(IssueType issueType) {
		this.issueType = issueType;
	}

	public String getIssueAddress() {
		return issueAddress;
	}

	public void setIssueAddress(String issueAddress) {
		this.issueAddress = issueAddress;
	}

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public Date getUseDate() {
		return useDate;
	}

	public void setUseDate(Date useDate) {
		this.useDate = useDate;
	}

	public boolean isUse() {
		return use;
	}

	public void setUse(boolean use) {
		this.use = use;
	}
	
}
