package net.oopscraft.application.common;

import java.security.MessageDigest;
import java.util.UUID;

public class EncodeUtility {
	
	public static String generateUUID() {
		return UUID.randomUUID().toString()
				.replaceAll("-", "")
				.toUpperCase();
	}
	
	/**
	 * MD5 hash
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public static String md5(String value) throws Exception {
		StringBuffer id = new StringBuffer(); 
		MessageDigest md = MessageDigest.getInstance("MD5"); 
		md.update(value.getBytes("UTF-8")); 
		byte byteData[] = md.digest();
		for(int i = 0 ; i < byteData.length ; i++){
			id.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
		}
		return id.toString();
	}

}
