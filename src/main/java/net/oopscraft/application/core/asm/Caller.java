package net.oopscraft.application.core.asm;

public class Caller {
	
	public static void send() {
		Callee.call("Hello");
	}

}
