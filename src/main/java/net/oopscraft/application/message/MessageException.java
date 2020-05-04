package net.oopscraft.application.message;

public class MessageException extends Exception {

	private static final long serialVersionUID = 668103176640687919L;
	
	String id;
	
	public MessageException(String id) {
		super(id);
		this.id = id;
	}
	
}
