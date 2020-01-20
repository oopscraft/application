package net.oopscraft.application.monitor;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.ThreadMXBean;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.oopscraft.application.common.process.ProcessExecutor;
import net.oopscraft.application.common.process.ProcessStreamHandler;
import net.oopscraft.application.monitor.MonitorInfo.ClassInfo;
import net.oopscraft.application.monitor.MonitorInfo.MemInfo;
import net.oopscraft.application.monitor.MonitorInfo.OsInfo;
import net.oopscraft.application.monitor.MonitorInfo.ThreadInfo;



public class MonitorAgent extends Observable implements Runnable {
	
	private static final Log LOG = LogFactory.getLog(MonitorAgent.class);
	private static MonitorAgent instance = null;
	private static int intervalSeconds = 3;
	private static int historySize = 10;

	private Thread thread = null;
	private List<MonitorInfo> monitorInfos = new CopyOnWriteArrayList<MonitorInfo>();
	
	/**
	 * getInstance
	 * @return
	 */
	public synchronized static MonitorAgent getInstance() throws Exception {
		synchronized(MonitorAgent.class) {
			if(instance == null) {
				instance = new MonitorAgent();
			}
			return instance;
		}
	}
	
	/**
	 * constructor
	 * @param interval
	 * @param limit
	 * @throws Exception
	 */
	private MonitorAgent() throws Exception {
		thread = new Thread(this);
		thread.setDaemon(true);
		thread.setPriority(Thread.MAX_PRIORITY);
		thread.start();
	}
	
	@Override
	public void run() {
		while(!Thread.interrupted()) {
			try {
				MonitorInfo monitorInfo = collectMonitorInfo();
				monitorInfos.add(monitorInfo);
				if(monitorInfos.size() > historySize) {
					monitorInfos.remove(0);
				}
				
				// notify
				setChanged();
				notifyObservers();
				
			}catch(Exception e) {
				LOG.warn(e.getMessage(), e);
			}finally {
				try { Thread.sleep(1000 * intervalSeconds); }catch(Exception ignore) {}
			}
		}
		
	}
	
	private MonitorInfo collectMonitorInfo() throws Exception {
		MonitorInfo monitorInfo = new MonitorInfo();
		try {
			// Getting OS info 
			OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
			monitorInfo.osInfo.put(OsInfo.name, osBean.getName());
			monitorInfo.osInfo.put(OsInfo.version, osBean.getVersion());
			monitorInfo.osInfo.put(OsInfo.arch, osBean.getArch());
			monitorInfo.osInfo.put(OsInfo.availableProcessors, osBean.getAvailableProcessors());
			monitorInfo.osInfo.put(OsInfo.systemLoadAverage, osBean.getSystemLoadAverage());
			
			// Getting memory info 
			MemoryMXBean memBean = ManagementFactory.getMemoryMXBean();
			monitorInfo.memInfo.put(MemInfo.heapMemoryUsage, memBean.getHeapMemoryUsage());
			monitorInfo.memInfo.put(MemInfo.nonHeapMemoryUsage, memBean.getNonHeapMemoryUsage());

			// Getting class loader info 
			ClassLoadingMXBean classBean = ManagementFactory.getClassLoadingMXBean();
			monitorInfo.classInfo.put(ClassInfo.totalLoadedClassCount, classBean.getTotalLoadedClassCount());
			monitorInfo.classInfo.put(ClassInfo.loadedClassCount, classBean.getLoadedClassCount());
			monitorInfo.classInfo.put(ClassInfo.unloadedClassCount, classBean.getUnloadedClassCount());
			
			// Getting thread info list threadInfoList.clear();
			ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
			long[] allThreadIds = threadBean.getAllThreadIds( );
			for(long threadId : allThreadIds) { 
				Map<ThreadInfo,Object> threadInfoMap = new LinkedHashMap<ThreadInfo,Object>();
				java.lang.management.ThreadInfo threadInfo = threadBean.getThreadInfo(threadId);
				threadInfoMap.put(ThreadInfo.threadId, threadInfo.getThreadId());
				threadInfoMap.put(ThreadInfo.threadName, threadInfo.getThreadName());
				threadInfoMap.put(ThreadInfo.threadState, threadInfo.getThreadState().name());
				threadInfoMap.put(ThreadInfo.waitedCount, threadInfo.getWaitedCount());
				threadInfoMap.put(ThreadInfo.waitedTime, threadInfo.getWaitedTime());
				threadInfoMap.put(ThreadInfo.blockCount, threadInfo.getBlockedCount());
				threadInfoMap.put(ThreadInfo.blockTime, threadInfo.getBlockedTime());
				monitorInfo.threadInfos.add(threadInfoMap);
			}
			
			// Getting process info via command
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
		    	topBuffer.append(e.getMessage());
		    } finally {
		    	monitorInfo.setTop(topBuffer.toString());
		    }
		}catch(Exception e) {
			LOG.warn(e.getMessage(),e);
			throw e;
		}
		return monitorInfo;
	}
	
	public MonitorInfo getMonitorInfo() {
		return monitorInfos.get(monitorInfos.size()-1);
	}
	
	public List<MonitorInfo> getMonitorInfos() {
		return monitorInfos;
	}
	
	public void addListener(MonitorListener monitorListener) {
		addObserver(monitorListener);
	}
	
	public void removeListener(MonitorListener monitorListener) {
		deleteObserver(monitorListener);
	}

}
