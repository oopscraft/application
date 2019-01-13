package net.oopscraft.application.core;

import java.util.UUID;

public class RandomUtils {
	
	public static String generateID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

}
