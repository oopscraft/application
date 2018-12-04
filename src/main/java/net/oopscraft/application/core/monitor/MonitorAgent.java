package net.oopscraft.application.core.monitor;

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

import net.oopscraft.application.core.monitor.MonitorInfo.ClassInfo;
import net.oopscraft.application.core.monitor.MonitorInfo.MemInfo;
import net.oopscraft.application.core.monitor.MonitorInfo.OsInfo;
import net.oopscraft.application.core.monitor.MonitorInfo.ThreadInfo;



public class MonitorAgent extends Observable implements Runnable {
	
	private static final Log LOG = LogFactory.getLog(MonitorAgent.class);
	private static MonitorAgent instance = null;
	private int intervalSeconds = 3;
	private int historySize = 10;

	private Thread thread = null;
	private List<MonitorInfo> monitorInfoList = new CopyOnWriteArrayList<MonitorInfo>();
	
	/**
	 * Initialize MonitorAgent
	 * @param intervalSeconds
	 * @param historySize
	 * @return
	 * @throws Exception
	 */
	public synchronized static MonitorAgent intialize(int intervalSeconds, int historySize) throws Exception {
		synchronized(MonitorAgent.class) {
			if(instance == null) {
				instance = new MonitorAgent(intervalSeconds, historySize);
			}else {
				throw new Exception("Monitor instance is already initialized");
			}
			return instance;
		}
	}
	
	/**
	 * getInstance
	 * @return
	 */
	public static MonitorAgent getInstance() throws Exception {
		synchronized(MonitorAgent.class) {
			if(instance == null) {
				throw new Exception("Monitor instance is not initialized."); 
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
	private MonitorAgent(int interval, int limit) throws Exception {
		this.intervalSeconds = interval;
		this.historySize = limit;
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
				monitorInfoList.add(monitorInfo);
				if(monitorInfoList.size() > historySize) {
					monitorInfoList.remove(0);
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
				monitorInfo.threadInfoList.add(threadInfoMap);
			} 
		}catch(Exception e) {
			LOG.warn(e.getMessage(),e);
			throw e;
		}
		return monitorInfo;
	}
	
	public List<MonitorInfo> getMonitorInfoList() {
		return monitorInfoList;
	}
	
	public void addListener(MonitorListener monitorListener) {
		addObserver(monitorListener);
	}
	
	public void removeListener(MonitorListener monitorListener) {
		deleteObserver(monitorListener);
	}

}
