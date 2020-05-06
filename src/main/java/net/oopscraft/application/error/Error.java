package net.oopscraft.application.error;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "APP_EROR_HIST")
public class Error {
	
	@Id
	@Column(name = "EROR_ID", length = 32)
	String id;
	
	@Column(name = "EROR_DATE", length = 32)
	Date date;

	@Column(name = "EXCP", length = 32)
	String exception;
	
	@Column(name = "MESG", length = 4000)
	String message;

	@JsonIgnore
	@Column(name = "TRAC", length = Integer.MAX_VALUE)
	@Lob
	String trace;
	
	@Column(name = "URI", length = 1024)
	String uri;
	
	@Column(name = "MTHD", length = 32)
	String method;
	
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

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTrace() {
		return trace;
	}

	public void setTrace(String trace) {
		this.trace = trace;
	}
	

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
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
