package net.oopscraft.application.util.asm;

public class Caller {
	
	public static void send() {
		Callee.call("Hello");
	}

}
