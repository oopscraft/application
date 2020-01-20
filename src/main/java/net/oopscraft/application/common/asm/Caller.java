package net.oopscraft.application.common.asm;

public class Caller {
	
	public static void send() {
		Callee.call("Hello");
	}

}
