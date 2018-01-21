/*
 * Copyright since 2002 oopscraft.net
 *
 * Everyone is permitted to copy and distribute verbatim copies of this license document, 
 * but changing it is not allowed.
 * Released under the LGPL-3.0 licence
 * https://opensource.org/licenses/lgpl-3.0.html
 */
package net.oopscraft.application.console.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/console/monitor")
public class MonitorController {
	
	@RequestMapping(value="")
	public ModelAndView getMonitor() throws Exception {
		ModelAndView modelAndView = new ModelAndView("console/monitor.tiles");
		return modelAndView;
	}

}
