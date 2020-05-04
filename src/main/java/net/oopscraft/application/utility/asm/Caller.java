package net.oopscraft.application.utility.asm;

public class Caller {
	
	public static void send() {
		Callee.call("Hello");
	}

}
