package net.oopscraft.application.core;

public class NotationUtils {
	
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
			if(previousChar == ' ' 
			|| previousChar == '-'
			|| previousChar == '_'
			){
				isCamelCase = true;
			}
			
			// Checks skip chars.
			if(currentChar == ' '
			|| currentChar == '-'
			|| currentChar == '_'
			){
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
			if(previousChar == ' ' 
			|| previousChar == '-'
			|| previousChar == '_'
			){
				isPascalCase = true;
			}
			
			// Checks skip chars.
			if(currentChar == ' '
			|| currentChar == '-'
			|| currentChar == '_'
			){
				continue;
			}
	
			// Checks already CamelCase.
			if(Character.isLowerCase(previousChar)
			&& Character.isUpperCase(currentChar)){
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
}
