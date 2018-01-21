/*
 * Copyright since 2002 oopscraft.net
 *
 * Everyone is permitted to copy and distribute verbatim copies of this license document, 
 * but changing it is not allowed.
 * Released under the LGPL-3.0 licence
 * https://opensource.org/licenses/lgpl-3.0.html
 */
package net.oopscraft.application.console;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import net.oopscraft.application.core.user.User;
import net.oopscraft.application.core.user.UserDao;

@Controller
@RequestMapping("/console/monitor")
public class MonitorController {
	
	@Autowired
	UserDao userDao;
	
	@RequestMapping(value="")
	public ModelAndView getMonitor() throws Exception {
		User user = userDao.selectUser("test");
		ModelAndView modelAndView = new ModelAndView("console/monitor.tiles");
		return modelAndView;
	}

}
