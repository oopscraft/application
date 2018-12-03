package net.oopscraft.application.core.monitor;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import net.oopscraft.application.core.monitor.MonitorInfo.DiskInfo;
import net.oopscraft.application.core.monitor.MonitorInfo.MemInfo;
import net.oopscraft.application.core.monitor.MonitorInfo.OsInfo;
import net.oopscraft.application.core.monitor.MonitorInfo.ThreadInfo;



public class MonitorAgent extends Observable implements Runnable {
	
	private static final Log LOG = LogFactory.getLog(MonitorAgent.class);
	private static MonitorAgent instance = null;
	private int intervalSeconds = 3;
	private int historySize = 10;

	private Thread thread = null;
	private List<MonitorInfo> jmxInfoHistory = new CopyOnWriteArrayList<MonitorInfo>();
	
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
				MonitorInfo jmxInfo = getJmxInfo();
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
	
	public static MonitorInfo getJmxInfo() throws Exception {
		MonitorInfo jmxInfo = new MonitorInfo();
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
			
			// Getting process info via command
		    String osName = System.getProperty("os.name").toLowerCase();
		    String command = null;
		    if(osName.contains("win")) {
		    	command = "cmd /C tasklist /FI \"STATUS eq running\" /V | sort /r /+65";
		    }else{
		    	command = "ps aux --sort -%mem";
		    }
		    Process process = null;
		    InputStream is = null;
		    InputStreamReader isr = null;
		    BufferedReader br = null;
		    try {
		    	process = Runtime.getRuntime().exec(command);
		    	is = process.getInputStream();
		    	isr = new InputStreamReader(is, "UTF-8");
		    	br = new BufferedReader(isr);
		    	String line = null;
		    	while((line = br.readLine()) != null) {
		    		jmxInfo.processInfoList.append(line).append(System.lineSeparator());
		    	}
		    }catch(Exception e) {
		    	throw e;
		    }finally {
		    	if(br != null) {
		    		try { br.close(); }catch(Exception ignore) {}
		    	}
		    	if(isr != null) {
		    		try { isr.close(); }catch(Exception ignore) {}
		    	}
		    	if(br != null) {
		    		try { is.close(); }catch(Exception ignore) {}
		    	}
		    	if(process != null) {
		    		process.destroy();
		    	}
		    }
		
		}catch(Exception e) {
			LOG.warn(e.getMessage(),e);
			throw e;
		}
		return jmxInfo;
	}
	
	public List<MonitorInfo> getJmxInfoHistory() {
		return jmxInfoHistory;
	}
	
	public MonitorInfo getLastestJmxInfo() {
		return jmxInfoHistory.get(jmxInfoHistory.size() -1);
	}
	
	public void addListener(MonitorAgentListener listener) {
		addObserver(listener);
	}
	
	public void removeListener(MonitorAgentListener listener) {
		deleteObserver(listener);
	}

}
