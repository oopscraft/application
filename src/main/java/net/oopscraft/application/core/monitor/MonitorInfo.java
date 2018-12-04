package net.oopscraft.application.core.monitor;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class MonitorInfo {

	public enum OsInfo {
		name,
		version,
		arch,
		availableProcessors,
		systemLoadAverage;
	}

	public enum MemInfo {
		heapMemoryUsage,
		nonHeapMemoryUsage;
	}

	public enum DiskInfo {
		totalSpace,
		freeSpace,
		usableSpace;
	}

	public enum ClassInfo {
		totalLoadedClassCount,
		loadedClassCount,
		unloadedClassCount;
	}

	public enum ThreadInfo {
		threadId,
		threadName,
		threadState,
		waitedCount, 
		waitedTime,
		blockCount,
		blockTime;
	}

	Date date = new Date();
	Map<OsInfo, Object> osInfo = new LinkedHashMap<OsInfo, Object>();
	Map<MemInfo, Object> memInfo = new LinkedHashMap<MemInfo, Object>();
	Map<ClassInfo, Object> classInfo = new LinkedHashMap<ClassInfo, Object>();
	List<Map<ThreadInfo, Object>> threadInfoList = new CopyOnWriteArrayList<Map<ThreadInfo, Object>>();
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setOsInfo(Map<OsInfo, Object> osInfo) {
		this.osInfo = osInfo;
	}

	public void setMemInfo(Map<MemInfo, Object> memInfo) {
		this.memInfo = memInfo;
	}

	public void setClassInfo(Map<ClassInfo, Object> classInfo) {
		this.classInfo = classInfo;
	}

	public void setThreadInfoList(List<Map<ThreadInfo, Object>> threadInfoList) {
		this.threadInfoList = threadInfoList;
	}

	public Map<OsInfo, Object> getOsInfo() {
		return osInfo;
	}

	public Map<MemInfo, Object> getMemInfo() {
		return memInfo;
	}

	public Map<ClassInfo, Object> getClassInfo() {
		return classInfo;
	}

	public List<Map<ThreadInfo, Object>> getThreadInfoList() {
		return threadInfoList;
	}

}