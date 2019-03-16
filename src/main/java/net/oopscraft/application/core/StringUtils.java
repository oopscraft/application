package net.oopscraft.application.core;

public class StringUtils {
	
	/**
	 * Checks value is empty
	 * @param value
	 * @return
	 */
	public static boolean isEmpty(String value) {
		if(value == null || value.trim().length() < 1) {
			return true;
		}
		return false;
	}
	
	/**
	 * Checks value is not empty
	 * @param value
	 * @return
	 */
	public static boolean isNotEmpty(String value) {
		if(value != null && value.trim().length() > 0) {
			return true;
		}
		return false;
	}
	
	
	/**
	 * Checks if delimiter char
	 * @param c
	 * @return
	 */
	private static boolean isDelimeterChar(char c) {
		if(c == ' '	|| c == '-' || c == '_'){
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * Convert string into camel case notation.
	 * @param name
	 * @return
	 */
	public static String toCamelCase(String name) {
		
		char[] chars = name.toCharArray();
		StringBuffer convertedName = new StringBuffer();
		int convertedNameLen = 0;
		for(int idx = 0; idx < chars.length; idx ++ ) {
			boolean isCamelCase = false;
			char previousChar = idx == 0 ? '\0' : chars[idx-1];
			char currentChar = chars[idx];			
			
			// Checks camel case.
			if(isDelimeterChar(previousChar)) {
				isCamelCase = true;
			}
			
			// Checks skip chars.
			if(isDelimeterChar(currentChar)) {
				continue;
			}
	
			// Checks already CamelCase.
			if(Character.isLowerCase(previousChar)
			&& Character.isUpperCase(currentChar)){
				isCamelCase = true;
			}
			
			// Appends char into camelCase StringBuffer.
			if(convertedNameLen != 0 && isCamelCase) {
				convertedName.append(String.valueOf(currentChar).toUpperCase());
			}else{
				convertedName.append(String.valueOf(currentChar).toLowerCase());
			}
			
			// Increases count.
			convertedNameLen ++;
		}
		
		return convertedName.toString();
	}
	
	/**
	 * Convert string into Pascal Case notation.
	 * @param name
	 * @return
	 */
	public static String toPascalCase(String name) {
		
		char[] chars = name.toCharArray();
		StringBuffer convertedName = new StringBuffer();
		for(int idx = 0; idx < chars.length; idx ++ ) {
			boolean isPascalCase = false;
			char previousChar = idx == 0 ? '\0' : chars[idx-1];
			char currentChar = chars[idx];			
			
			// Checks camel case.
			if(isDelimeterChar(previousChar)) {
				isPascalCase = true;
			}
			
			// Checks skip chars.
			if(isDelimeterChar(currentChar)) {
				continue;
			}
	
			// Checks already CamelCase.
			if(Character.isLowerCase(previousChar) && Character.isUpperCase(currentChar)){
				isPascalCase = true;
			}
			
			if(idx == 0) {
				isPascalCase = true;
			}
			
			// Appends char into camelCase StringBuffer.
			if(isPascalCase) {
				convertedName.append(String.valueOf(currentChar).toUpperCase());
			}else{
				convertedName.append(String.valueOf(currentChar).toLowerCase());
			}
		}
		
		return convertedName.toString();
	}
	
	/**
	 * Remove line break and white space chars.
	 * @param value
	 * @return
	 */
	public static String stripWhitespace(String value) {
        if(value == null || value.length() <= 2) {
            return value;
        }
        StringBuffer b = new StringBuffer(value.length());
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (Character.isWhitespace(c)) {
                if (i > 0 && !Character.isWhitespace(value.charAt(i - 1))) {
                    b.append(' ');
                }
            } else {
                b.append(c);
            }
        }
        return b.toString();
	}
	
	/**
	 * Makes string to ellipsis
	 * @param value
	 * @param size
	 * @return
	 */
	public static String toEllipsis(String value, int size) {
		if(value == null) {
			return null;
		}
		if(value.length() > size) {
			return value.substring(0, value.length()-3) + "...";
		}else {
			return value;
		}
	}
}
