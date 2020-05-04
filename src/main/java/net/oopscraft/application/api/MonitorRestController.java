package net.oopscraft.application.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import net.oopscraft.application.monitor.Monitor;
import net.oopscraft.application.monitor.MonitorService;

@CrossOrigin
@RestController
@RequestMapping("/api/monitors")
public class MonitorRestController {

	@Autowired
	MonitorService monitorService;
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<Monitor> getMonitors() throws Exception {
		return monitorService.getMonitors();
	}
	
}
