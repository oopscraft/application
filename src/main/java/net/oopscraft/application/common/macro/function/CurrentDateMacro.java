/*
 * Copyright since 2002 oopscraft.net
 *
 * Everyone is permitted to copy and distribute verbatim copies of this license document, 
 * but changing it is not allowed.
 * Released under the LGPL-3.0 licence
 * https://opensource.org/licenses/lgpl-3.0.html
 */
package net.oopscraft.application.common.macro.function;

import net.oopscraft.application.common.ValueMap;
import net.oopscraft.application.common.macro.Macro;
import net.oopscraft.application.common.macro.MacroMeta;

/**
 * @author chomookun@gmail.com
 *
 */
@MacroMeta(name="CURRENT_DATE",syntax="CURRENT_DATE('yyyyMMdd')", example="")
public class CurrentDateMacro extends Macro {

	/* (non-Javadoc)
	 * @see net.oopscraft.application.core.macro.Macro#execute(net.oopscraft.application.core.ValueMap, java.lang.String[])
	 */
	@Override
	public String execute(ValueMap context, String... args) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
