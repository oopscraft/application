package net.oopscraft.application.admin;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import net.oopscraft.application.core.JsonConverter;
import net.oopscraft.application.monitor.MonitorAgent;
import net.oopscraft.application.monitor.MonitorInfo;

@PreAuthorize("hasAuthority('ADMIN_MONITOR')")
@Controller
@RequestMapping("/admin/monitor")
public class MonitorController {
	
	@RequestMapping(method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ModelAndView dash() throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("admin/monitor.tiles");
		return modelAndView;
	}

	/**
	 * getMonitorInfos
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getMonitorInfos", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String getMonitorInfos() throws Exception {
		List<MonitorInfo> monitorInfos = MonitorAgent.getInstance().getMonitorInfos();
		return JsonConverter.toJson(monitorInfos);
	}

}
