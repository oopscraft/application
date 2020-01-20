/*
 * Copyright since 2002 oopscraft.net
 *
 * Everyone is permitted to copy and distribute verbatim copies of this license document, 
 * but changing it is not allowed.
 * Released under the LGPL-3.0 licence
 * https://opensource.org/licenses/lgpl-3.0.html
 */
package net.oopscraft.application.common.captcha;

/**
 * @author chomookun@gmail.com
 *
 */
public class AnswerGenerator {
	
	public static enum Type { CHAR, NUMERIC, CHAR_NUMERIC }
	
	public static String generateAlphabet(int size) {
		StringBuffer buffer = new StringBuffer();
		for(int i = 1; i <= size; i ++ ) {
			char c = (char) ((Math.random() * 26) + 65);
			buffer.append(c);
		}
		return buffer.toString();
	}
	
	public static String generateNumeric(int size) {
		StringBuffer buffer = new StringBuffer();
		for(int i = 1; i <= size; i ++ ) {
			int n = (int) (Math.random() * 10);
			buffer.append(n);
		}
		return buffer.toString();
	}
	
	public static String generateAlphabetNumeric(int size) {
		StringBuffer buffer = new StringBuffer();
		for(int i = 1; i <= size; i ++ ) {
			if(i%2 == 1) {
				char c = (char) ((Math.random() * 26) + 65);
				buffer.append(c);
			}else {
				int n = (int) (Math.random() * 10);
				buffer.append(n);
			}
		}
		return buffer.toString();
	}
	
}
