package net.oopscraft.application.common.macro;

import net.oopscraft.application.common.ValueMap;

/**
 * Macro abstract class
 * @author chomookun@gmail.com
 */
public abstract class Macro { 

	/**
	 * Executes macro function abstract method
	 * @param context
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public abstract String execute(ValueMap context, String... args) throws Exception; 

}

