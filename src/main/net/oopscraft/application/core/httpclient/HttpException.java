package net.oopscraft.application.core.httpclient; 

public class HttpException extends Exception { 
	private static final long serialVersionUID = -6708475225195828436L; 
	int httpCode = 0; 
	
	public HttpException(Throwable cause) { 
		super(cause); 
	}
	
	public HttpException(int httpCode, String message) { 
		super(message); 
		this.httpCode = httpCode; 
	} 

	public HttpException(int httpCode, String message, Throwable cause) { 
		super(message, cause); 
		this.httpCode = httpCode; 
	} 
	
	public int getHttpCode() { 
		return httpCode; 
	} 
	
}