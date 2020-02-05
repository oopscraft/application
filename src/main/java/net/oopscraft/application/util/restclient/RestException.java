package net.oopscraft.application.util.restclient; 

public class RestException extends Exception { 
	private static final long serialVersionUID = -6708475225195828436L;
	
	int httpCode = 0; 
	
	public RestException(Throwable cause) { 
		super(cause); 
	}
	
	public RestException(int httpCode, String message) { 
		super(message); 
		this.httpCode = httpCode; 
	} 

	public RestException(int httpCode, String message, Throwable cause) { 
		super(message, cause); 
		this.httpCode = httpCode; 
	} 
	
	public int getHttpCode() { 
		return httpCode; 
	} 
	
}