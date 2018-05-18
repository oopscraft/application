package net.oopscraft.application.core;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class SystemInfo {
		
	public enum OsInfo { name, version, arch, availableProcessors, systemLoadAverage }
	public enum MemInfo { heapMemoryUsage, nonHeapMemoryUsage }
	public enum DiskInfo {	totalSpace, freeSpace, usableSpace }
	public enum ClassInfo { totalLoadedClassCount, loadedClassCount, unloadedClassCount }
	public enum ThreadInfo { threadId, threadName, threadState, waitedCount, waitedTime, blockCount, blockTime }

	Map<OsInfo, Object> osInfo = new LinkedHashMap<OsInfo, Object>();
	Map<MemInfo, Object> memInfo = new LinkedHashMap<MemInfo, Object>();
	Map<DiskInfo, Object> diskInfo = new LinkedHashMap<DiskInfo, Object>();
	Map<ClassInfo, Object> classInfo = new LinkedHashMap<ClassInfo, Object>();
	List<Map<ThreadInfo, Object>> threadInfoList = new CopyOnWriteArrayList<Map<ThreadInfo, Object>>();

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