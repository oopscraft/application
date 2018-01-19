package net.oopscraft.application.core.monitor;

import java.io.File;
import java.lang.management.ClassLoadingMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.ThreadMXBean;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JmxInfo {

	private static final Log LOG = LogFactory.getLog(JmxInfo.class);
	
	enum OsInfo { name, version, arch, availableProcessors, systemLoadAverage }
	Map<OsInfo, Object> osInfo = new LinkedHashMap<OsInfo, Object>();

	enum MemInfo { heapMemoryUsage, nonHeapMemoryUsage }
	Map<MemInfo, Object> memInfo = new LinkedHashMap<MemInfo, Object>();

	enum DiskInfo {	totalSpace, freeSpace, usableSpace }
	Map<DiskInfo, Object> diskInfo = new LinkedHashMap<DiskInfo, Object>();

	enum ClassInfo { totalLoadedClassCount, loadedClassCount, unloadedClassCount }
	Map<ClassInfo, Object> classInfo = new LinkedHashMap<ClassInfo, Object>();

	enum ThreadInfo { threadId, threadName, threadState, waitedCount, waitedTime, blockCount, blockTime }
	List<Map<ThreadInfo, Object>> threadInfoList = new CopyOnWriteArrayList<Map<ThreadInfo, Object>>();

	public JmxInfo() throws Exception {
		try {
			// Getting OS info 
			OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
			osInfo.put(OsInfo.name, osBean.getName());
			osInfo.put(OsInfo.version, osBean.getVersion());
			osInfo.put(OsInfo.arch, osBean.getArch());
			osInfo.put(OsInfo.availableProcessors, osBean.getAvailableProcessors());
			osInfo.put(OsInfo.systemLoadAverage, 	osBean.getSystemLoadAverage());
			
			// Getting memory info 
			MemoryMXBean memBean = ManagementFactory.getMemoryMXBean();
			memInfo.put(MemInfo.heapMemoryUsage, memBean.getHeapMemoryUsage());
			memInfo.put(MemInfo.nonHeapMemoryUsage, memBean.getNonHeapMemoryUsage());
			
			// Getting disk info 
			File file = new File("/");
			diskInfo.put(DiskInfo.totalSpace, file.getTotalSpace());
			diskInfo.put(DiskInfo.freeSpace, file.getFreeSpace());
			diskInfo.put(DiskInfo.usableSpace, file.getUsableSpace());
			
			// Getting class loader info 
			ClassLoadingMXBean classBean = ManagementFactory.getClassLoadingMXBean();
			classInfo.put(ClassInfo.totalLoadedClassCount, classBean.getTotalLoadedClassCount());
			classInfo.put(ClassInfo.loadedClassCount, classBean.getLoadedClassCount());
			classInfo.put(ClassInfo.unloadedClassCount, classBean.getUnloadedClassCount());
			
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
				threadInfoList.add(threadInfoMap);
			} 
		
		}catch(Exception ignore) {
			LOG.warn(ignore.getMessage(),ignore);
		}finally{ 
			LOG.debug("Monitor.check.");
		}
	}

	public Map<OsInfo, Object> getOsInfo() {
		return osInfo;
	}

	public Map<MemInfo, Object> getMemInfo() {
		return memInfo;
	}

	public Map<DiskInfo, Object> getDiskInfo() {
		return diskInfo;
	}

	public Map<ClassInfo, Object> getClassInfo() {
		return classInfo;
	}

	public List<Map<ThreadInfo, Object>> getThreadInfoList() {
		return threadInfoList;
	}
	
	

}