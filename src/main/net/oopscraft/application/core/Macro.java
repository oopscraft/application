package net.oopscraft.application.core;

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

