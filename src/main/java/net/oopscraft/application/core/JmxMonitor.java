package net.oopscraft.application.core;

import java.io.File;
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

import net.oopscraft.application.core.JmxInfo.ClassInfo;
import net.oopscraft.application.core.JmxInfo.DiskInfo;
import net.oopscraft.application.core.JmxInfo.MemInfo;
import net.oopscraft.application.core.JmxInfo.OsInfo;
import net.oopscraft.application.core.JmxInfo.ThreadInfo;



public class JmxMonitor extends Observable implements Runnable {
	
	private static final Log LOG = LogFactory.getLog(JmxMonitor.class);
	private static JmxMonitor instance = null;
	private int intervalSeconds = 3;
	private int historySize = 10;

	private Thread thread = null;
	private List<JmxInfo> jmxInfoHistory = new CopyOnWriteArrayList<JmxInfo>();
	
	/**
	 * Initialize MonitorAgent
	 * @param intervalSeconds
	 * @param historySize
	 * @return
	 * @throws Exception
	 */
	public synchronized static JmxMonitor intialize(int intervalSeconds, int historySize) throws Exception {
		synchronized(JmxMonitor.class) {
			if(instance == null) {
				instance = new JmxMonitor(intervalSeconds, historySize);
			}
			return instance;
		}
	}
	
	/**
	 * getInstance
	 * @return
	 */
	public static JmxMonitor getInstance() throws Exception {
		synchronized(JmxMonitor.class) {
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
	private JmxMonitor(int interval, int limit) throws Exception {
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
				JmxInfo jmxInfo = getJmxInfo();
				jmxInfoHistory.add(jmxInfo);
				if(jmxInfoHistory.size() > historySize) {
					jmxInfoHistory.remove(0);
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
	
	public static JmxInfo getJmxInfo() throws Exception {
		JmxInfo jmxInfo = new JmxInfo();
		try {
			// Getting OS info 
			OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
			jmxInfo.osInfo.put(OsInfo.name, osBean.getName());
			jmxInfo.osInfo.put(OsInfo.version, osBean.getVersion());
			jmxInfo.osInfo.put(OsInfo.arch, osBean.getArch());
			jmxInfo.osInfo.put(OsInfo.availableProcessors, osBean.getAvailableProcessors());
			jmxInfo.osInfo.put(OsInfo.systemLoadAverage, 	osBean.getSystemLoadAverage());
			
			// Getting memory info 
			MemoryMXBean memBean = ManagementFactory.getMemoryMXBean();
			jmxInfo.memInfo.put(MemInfo.heapMemoryUsage, memBean.getHeapMemoryUsage());
			jmxInfo.memInfo.put(MemInfo.nonHeapMemoryUsage, memBean.getNonHeapMemoryUsage());
			
			// Getting disk info 
			File file = new File("/");
			jmxInfo.diskInfo.put(DiskInfo.totalSpace, file.getTotalSpace());
			jmxInfo.diskInfo.put(DiskInfo.freeSpace, file.getFreeSpace());
			jmxInfo.diskInfo.put(DiskInfo.usableSpace, file.getUsableSpace());
			
			// Getting class loader info 
			ClassLoadingMXBean classBean = ManagementFactory.getClassLoadingMXBean();
			jmxInfo.classInfo.put(ClassInfo.totalLoadedClassCount, classBean.getTotalLoadedClassCount());
			jmxInfo.classInfo.put(ClassInfo.loadedClassCount, classBean.getLoadedClassCount());
			jmxInfo.classInfo.put(ClassInfo.unloadedClassCount, classBean.getUnloadedClassCount());
			
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
				jmxInfo.threadInfoList.add(threadInfoMap);
			} 
		
		}catch(Exception e) {
			LOG.warn(e.getMessage(),e);
			throw e;
		}
		return jmxInfo;
	}
	
	public List<JmxInfo> getJmxInfoHistory() {
		return jmxInfoHistory;
	}
	
	public JmxInfo getLastestJmxInfo() {
		return jmxInfoHistory.get(jmxInfoHistory.size() -1);
	}
	
	public void addListener(JmxMonitorListener listener) {
		addObserver(listener);
	}
	
	public void removeListener(JmxMonitorListener listener) {
		deleteObserver(listener);
	}

}
