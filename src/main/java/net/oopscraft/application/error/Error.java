package net.oopscraft.application.error;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(
	name = "APP_EROR_HIST",
	indexes = {
		@Index(columnList="EROR_DATE")
	}
)
public class Error {
	
	@Id
	@Column(name = "EROR_ID", length = 32)
	String id;
	
	@Column(name = "EROR_DATE", length = 32)
	Date date;

	@Column(name = "EXCP_CLAS", length = 32)
	String exceptionClass;
	
	@Column(name = "EXCP_MESG", length = 4000)
	String exceptionMessage;

	@JsonIgnore
	@Column(name = "EXCP_TRCE", length = Integer.MAX_VALUE)
	@Lob
	String exceptionTrace;
	
	@Column(name = "REQ_URI", length = 1024)
	String requestUri;
	
	@Column(name = "REQ_MTHD", length = 32)
	String requestMethod;
	
	@Column(name = "QURY_STRG", length = Integer.MAX_VALUE)
	@Lob
	String queryString;
	
	@Column(name = "STAT_CODE")
	Integer statusCode;
	

	public Error() {}
	
	public Error(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getExceptionClass() {
		return exceptionClass;
	}

	public void setExceptionClass(String exceptionClass) {
		this.exceptionClass = exceptionClass;
	}

	public String getExceptionMessage() {
		return exceptionMessage;
	}

	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}

	public String getExceptionTrace() {
		return exceptionTrace;
	}

	public void setExceptionTrace(String exceptionTrace) {
		this.exceptionTrace = exceptionTrace;
	}

	public String getRequestUri() {
		return requestUri;
	}

	public void setRequestUri(String requestUri) {
		this.requestUri = requestUri;
	}

	public String getRequestMethod() {
		return requestMethod;
	}

	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}
	
}
