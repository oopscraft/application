package net.oopscraft.application.monitor;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import net.oopscraft.application.api.WebSocketHandler;
import net.oopscraft.application.core.JsonConverter;
import net.oopscraft.application.core.ValueMap;
import net.oopscraft.application.core.process.ProcessExecutor;
import net.oopscraft.application.core.process.ProcessStreamHandler;
import net.oopscraft.application.monitor.entity.Monitor;
import net.oopscraft.application.monitor.entity.Monitor.MemoryKey;
import net.oopscraft.application.monitor.entity.Monitor.OperatingSystemKey;

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
	@Scheduled(fixedDelay=1000)	
	public void collectMonitor() throws Exception {
		Monitor monitor = new Monitor(new Date());
		
		// getting top string
		monitor.setTop(getTop());
		
		// getting OperatingSystem info 
		OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
		ValueMap operatingSystem = new ValueMap();
		operatingSystem.put(OperatingSystemKey.name.name(), operatingSystemMXBean.getName());
		operatingSystem.put(OperatingSystemKey.version.name(), operatingSystemMXBean.getVersion());
		operatingSystem.put(OperatingSystemKey.arch.name(), operatingSystemMXBean.getArch());
		operatingSystem.put(OperatingSystemKey.availableProcessors.name(), operatingSystemMXBean.getAvailableProcessors());
		double systemLoadAverage = operatingSystemMXBean.getSystemLoadAverage();
		if(systemLoadAverage == -1 && operatingSystemMXBean instanceof com.sun.management.OperatingSystemMXBean) {
			systemLoadAverage = ((com.sun.management.OperatingSystemMXBean)operatingSystemMXBean).getSystemCpuLoad();
		}
		operatingSystem.put(OperatingSystemKey.systemLoadAverage.name(), systemLoadAverage);
		monitor.setOperatingSystem(operatingSystem);
		
		// getting memory info
		MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
		ValueMap memory = new ValueMap();
		memory.put(MemoryKey.heapMemoryUsage.name(), memoryMXBean.getHeapMemoryUsage());
		memory.put(MemoryKey.nonHeapMemoryUsage.name(), memoryMXBean.getNonHeapMemoryUsage());
		monitor.setMemory(memory);
		
		
		
//		monitorInfo.osInfo.put(OsInfo.name, osBean.getName());
//		monitorInfo.osInfo.put(OsInfo.version, osBean.getVersion());
//		monitorInfo.osInfo.put(OsInfo.arch, osBean.getArch());
//		monitorInfo.osInfo.put(OsInfo.availableProcessors, osBean.getAvailableProcessors());
//		monitorInfo.osInfo.put(OsInfo.systemLoadAverage, osBean.getSystemLoadAverage());
//		
//		// Getting memory info 
//		MemoryMXBean memBean = ManagementFactory.getMemoryMXBean();
//		monitorInfo.memInfo.put(MemInfo.heapMemoryUsage, memBean.getHeapMemoryUsage());
//		monitorInfo.memInfo.put(MemInfo.nonHeapMemoryUsage, memBean.getNonHeapMemoryUsage());
//
//		// Getting class loader info 
//		ClassLoadingMXBean classBean = ManagementFactory.getClassLoadingMXBean();
//		monitorInfo.classInfo.put(ClassInfo.totalLoadedClassCount, classBean.getTotalLoadedClassCount());
//		monitorInfo.classInfo.put(ClassInfo.loadedClassCount, classBean.getLoadedClassCount());
//		monitorInfo.classInfo.put(ClassInfo.unloadedClassCount, classBean.getUnloadedClassCount());
//		
//		// Getting thread info list threadInfoList.clear();
//		ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
//		long[] allThreadIds = threadBean.getAllThreadIds( );
//		for(long threadId : allThreadIds) { 
//			Map<ThreadInfo,Object> threadInfoMap = new LinkedHashMap<ThreadInfo,Object>();
//			java.lang.management.ThreadInfo threadInfo = threadBean.getThreadInfo(threadId);
//			threadInfoMap.put(ThreadInfo.threadId, threadInfo.getThreadId());
//			threadInfoMap.put(ThreadInfo.threadName, threadInfo.getThreadName());
//			threadInfoMap.put(ThreadInfo.threadState, threadInfo.getThreadState().name());
//			threadInfoMap.put(ThreadInfo.waitedCount, threadInfo.getWaitedCount());
//			threadInfoMap.put(ThreadInfo.waitedTime, threadInfo.getWaitedTime());
//			threadInfoMap.put(ThreadInfo.blockCount, threadInfo.getBlockedCount());
//			threadInfoMap.put(ThreadInfo.blockTime, threadInfo.getBlockedTime());
//			monitorInfo.threadInfos.add(threadInfoMap);
//		}
		
		
//		// JMX info
//		monitor.setOperatingSystem(ManagementFactory.getOperatingSystemMXBean());
//		monitor.setMemory(ManagementFactory.getMemoryMXBean());
//		monitor.setClassLoading(ManagementFactory.getClassLoadingMXBean());
//		List<ThreadInfo> threadInfos = new ArrayList<ThreadInfo>();
//		ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
//		long[] allThreadIds = threadBean.getAllThreadIds();
//		for(long threadId : allThreadIds) { 
//			ThreadInfo threadInfo = threadBean.getThreadInfo(threadId);
//			threadInfos.add(threadInfo);
//		}
//		monitor.setThreadInfos(threadInfos);
		
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
