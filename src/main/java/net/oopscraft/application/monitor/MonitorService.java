package net.oopscraft.application.monitor;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import net.oopscraft.application.api.WebSocketHandler;
import net.oopscraft.application.core.JsonConverter;
import net.oopscraft.application.core.process.ProcessExecutor;
import net.oopscraft.application.core.process.ProcessStreamHandler;
import net.oopscraft.application.monitor.entity.Monitor;

@Service
public class MonitorService {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(MonitorService.class);
	private final static int HISTORY_SIZE = 10;
	
	private List<Monitor> monitors = new CopyOnWriteArrayList<Monitor>();
	
	@Autowired
	WebSocketHandler webSocketHandler;
	
	/**
	 * Scheduled collecting monitor info
	 * @throws Exception
	 */
	@Scheduled(fixedDelay=1000*3)
	public void collectMonitor() throws Exception {
		Monitor monitor = new Monitor();
//		monitor.setOperatingSystem(ManagementFactory.getOperatingSystemMXBean());
//		monitor.setMemory(ManagementFactory.getMemoryMXBean());
//		monitor.setClassLoading(ManagementFactory.getClassLoadingMXBean());
		List<ThreadInfo> threadInfos = new ArrayList<ThreadInfo>();
		ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
		long[] allThreadIds = threadBean.getAllThreadIds();
		for(long threadId : allThreadIds) { 
			ThreadInfo threadInfo = threadBean.getThreadInfo(threadId);
			threadInfos.add(threadInfo);
		}
		monitor.setThreadInfos(threadInfos);
		
		// getting top string
		monitor.setTop(getTop());
		
		// adds list
		monitors.add(monitor);
		if(monitors.size() > HISTORY_SIZE) {
			monitors.remove(0);
		}
		
		// sends message
		webSocketHandler.broadcastMessage(JsonConverter.toJson(monitor));
	}

	/**
	 * Getting process info via command
	 * @return
	 */
	private String getTop() {
	    final StringBuffer topBuffer = new StringBuffer();
	    ProcessExecutor processExecutor = new ProcessExecutor();
	    try {
		    String osName = System.getProperty("os.name").toLowerCase();
		    if(osName.contains("win")) {
			    processExecutor.setCommand("cmd /C tasklist /FI \"STATUS eq running\" /V | sort /r /+65");
		    }else{
		    	//processExecutor.setCommand("top -b -n1 -c");
		    	processExecutor.setCommand(new String[] {
		    		"/bin/sh"
		    		,"-c"
		    		,"ps aux --sort -%cpu,%mem | head -30" 
		    	});
		    }
		    processExecutor.setProcessStreamHandler(new ProcessStreamHandler() {
				@Override
				public void readLine(String line) {
					topBuffer.append(line).append(System.lineSeparator());
				}
		    });
		    processExecutor.execute();
	    }catch(Exception e) {
	    	LOGGER.error(e.getMessage(),e);
	    	topBuffer.append(e.getMessage());
	    }
	    return topBuffer.toString();
	}
	
	/**
	 * Returns monitors
	 * @return
	 */
	public List<Monitor> getMonitors() {
		return monitors;
	}

}
