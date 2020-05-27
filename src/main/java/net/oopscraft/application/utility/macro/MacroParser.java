package net.oopscraft.application.utility.macro; 

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.text.CaseUtils;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import net.oopscraft.application.core.ValueMap; 

public class MacroParser { 
	
	private static final String PARAM_IDENTIFIER = "(?:\\#\\{)([^\\#\\{\\}]*)(?:\\})";
	private static final String MACRO_IDENTIFIER = "\\$\\{(.*?)\\}"; 
	private static final Pattern MACRO_PATTERN = Pattern.compile("([a-zA-Z_]+)\\((.*)\\)");

	private String macroPackage = null;		// Package path of macro classes 
	
	/**
	 * Constructor
	 * @param macroPackage Package path of macro class
	 */
	public MacroParser(String macroPackage) { 
		this.macroPackage = macroPackage; 
	} 

	/**
	 * Returns macro list
	 * @return
	 * @throws Exception
	 */
	public List<ValueMap> getMacroList() throws Exception { 
		List<ValueMap> macroList = new ArrayList<ValueMap>(); 
		Reflections reflections = new Reflections(macroPackage, new SubTypesScanner()); 
		Set<Class<? extends Macro>> macroClassSet = reflections.getSubTypesOf(Macro.class); 
		for(Class<? extends Macro> macroClass : macroClassSet) { 
			MacroMeta macroMeta = macroClass.getAnnotation(MacroMeta.class); 
			if(macroMeta != null) { 
				ValueMap macro = new ValueMap(); 
				macro.setString("class", macroClass.getName()); 
				macro.setString("name", macroMeta.name()); 
				macro.setString("syntax", macroMeta.syntax()); 
				macroList.add(macro); 
			} 
		} 
		return macroList; 
	}
	
	/**
	 * parse
	 * @param context
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public String parse(ValueMap context, String value) throws Exception {
		value = parseParam(context, value);
		value = parseMacro(context, value);
		return value;
	}
	
	/**
	 * parseParam
	 * @param macroContext
	 * @param value
	 * @return
	 * @throws Exception
	 */
	private String parseParam(ValueMap context, String value) throws Exception {
		Pattern p = Pattern.compile(PARAM_IDENTIFIER);
        Matcher m = p.matcher(value);
        StringBuffer sb = new StringBuffer();
        while(m.find()) {
            String originalValue = m.group(1);
            originalValue = context.getString(originalValue);
            m.appendReplacement(sb, Matcher.quoteReplacement(originalValue));
        }
        m.appendTail(sb);
       return sb.toString();
	}
	
	/**
	 * macroContext
	 * @param macroContext
	 * @param value
	 * @return
	 * @throws Exception
	 */
	private String parseMacro(ValueMap macroContext, String value) throws Exception {
        Pattern p = Pattern.compile(MACRO_IDENTIFIER);
        Matcher m = p.matcher(value);
        StringBuffer sb = new StringBuffer();
        while(m.find()) {
            String originalValue = m.group(1);
            String parsedValue = executeMacro(macroContext, originalValue);
            m.appendReplacement(sb, Matcher.quoteReplacement(parsedValue));
        }
        m.appendTail(sb);
        String parsedValue = sb.toString();
        return parsedValue;
	}
	
	/**
	 * Executes macro code
	 * @param macroExpression
	 * @return
	 * @throws Exception
	 */
	private String executeMacro(ValueMap macroContext, String macroExpression) throws Exception { 
		StringBuffer buffer = new StringBuffer(); 
		Matcher matcher = MACRO_PATTERN.matcher(macroExpression); 
		while(matcher.find()) { 
			String macroName = matcher.group(1); 
			String argumentClause = matcher.group(2); 
			argumentClause = executeMacro(macroContext, argumentClause); 
			// execute macro 
			String macroClassName = String.format("%s.%sMacro", this.macroPackage, CaseUtils.toCamelCase(macroName, true)); 
			Class<?> macroClass = Class.forName(macroClassName); 
			Macro macro = (Macro) macroClass.newInstance(); 
			String result = macro.execute(macroContext, splitArgument(argumentClause)); 
			matcher.appendReplacement(buffer, Matcher.quoteReplacement(result)); 
		} 
		matcher.appendTail(buffer); 
		return buffer.toString();
	} 
	
	/** 
	 * split argument clauses. 
	 * @param argumentClause 
	 * @return 
	 */ 
	private String[] splitArgument(String argumentClause) { 
		int depth = 0; 
		List<String> argumentList = new ArrayList<String>(); 
		StringBuffer buffer = new StringBuffer(); 
		for(char c : argumentClause.toCharArray()) { 
			buffer.append(c); 
			if(c == '('){ 
				depth ++; 
			}else if(c == ')'){ 
				depth --; 
			} 
			if(depth == 0 && c == ',') { 
				buffer.deleteCharAt(buffer.length()-1); 
				argumentList.add(buffer.toString()); 
				buffer = new StringBuffer(); 
			} 
		} 
		argumentList.add(buffer.toString()); 

		// strips " ' characters.
		for(int i = 0; i < argumentList.size(); i ++) {
			String argument = argumentList.get(i);
			argument = argument.replaceAll("(^[\'\"])|([\'\"]$)", "");
			argumentList.set(i, argument);
		}
		
		return argumentList.toArray(new String[argumentList.size()]); 
	}

	
}
