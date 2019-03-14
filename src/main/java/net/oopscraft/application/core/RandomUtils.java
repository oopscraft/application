package net.oopscraft.application.core;

import java.util.UUID;

public class RandomUtils {
	
	public static String generateUUID() {
		return UUID.randomUUID().toString()
				.replaceAll("-", "")
				.toUpperCase();
	}

}
