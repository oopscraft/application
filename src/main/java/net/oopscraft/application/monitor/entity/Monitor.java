package net.oopscraft.application.monitor.entity;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.ThreadInfo;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.oopscraft.application.core.ValueMap;

public class Monitor {
	
	public enum OperatingSystemKey {
		name,
		version,
		arch,
		availableProcessors,
		systemLoadAverage;
	}
	
	public enum MemoryKey {
		heapMemoryUsage,
		nonHeapMemoryUsage;
	}
	
	public enum ClassLoadingKey {
		totalLoadedClassCount,
		loadedClassCount,
		unloadedClassCount;
	}
	
	public enum ThreadInfoKey {
		threadId,
		threadName,
		threadState,
		waitedCount, 
		waitedTime,
		blockCount,
		blockTime;
	}
		
	Date date;
	String top;
	ValueMap operatingSystem;
	ValueMap memory;
	ValueMap classLoading;
	List<ValueMap> threadInfos;
	
	public Monitor(Date date) {
		this.date = date;
	}
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getTop() {
		return top;
	}

	public void setTop(String top) {
		this.top = top;
	}

	public ValueMap getOperatingSystem() {
		return operatingSystem;
	}

	public void setOperatingSystem(ValueMap operatingSystem) {
		this.operatingSystem = operatingSystem;
	}

	public ValueMap getMemory() {
		return memory;
	}

	public void setMemory(ValueMap memory) {
		this.memory = memory;
	}

	public ValueMap getClassLoading() {
		return classLoading;
	}

	public void setClassLoading(ValueMap classLoading) {
		this.classLoading = classLoading;
	}

	public List<ValueMap> getThreadInfos() {
		return threadInfos;
	}

	public void setThreadInfos(List<ValueMap> threadInfos) {
		this.threadInfos = threadInfos;
	}
	

	
	

	
	
	
	
	
	
	
	
	
	
	
	
	

//	public enum OsInfo {
//		name,
//		version,
//		arch,
//		availableProcessors,
//		systemLoadAverage;
//	}
//
//	public enum MemInfo {
//		heapMemoryUsage,
//		nonHeapMemoryUsage;
//	}
//
//	public enum ClassInfo {
//		totalLoadedClassCount,
//		loadedClassCount,
//		unloadedClassCount;
//	}
//
//	public enum ThreadInfo {
//		threadId,
//		threadName,
//		threadState,
//		waitedCount, 
//		waitedTime,
//		blockCount,
//		blockTime;
//	}
//
//	Date date = new Date();
//	String top;
//	public Map<OsInfo, Object> osInfo = new LinkedHashMap<OsInfo, Object>();
//	public Map<MemInfo, Object> memInfo = new LinkedHashMap<MemInfo, Object>();
//	public Map<ClassInfo, Object> classInfo = new LinkedHashMap<ClassInfo, Object>();
//	public List<Map<ThreadInfo, Object>> threadInfos = new CopyOnWriteArrayList<Map<ThreadInfo, Object>>();
//	
//	public Date getDate() {
//		return date;
//	}
//
//	public void setTop(String top) {
//		this.top = top;
//	}
//	
//	public String getTop() {
//		return this.top;
//	}
//	
//	public void setDate(Date date) {
//		this.date = date;
//	}
//
//	public void setOsInfo(Map<OsInfo, Object> osInfo) {
//		this.osInfo = osInfo;
//	}
//
//	public void setMemInfo(Map<MemInfo, Object> memInfo) {
//		this.memInfo = memInfo;
//	}
//
//	public void setClassInfo(Map<ClassInfo, Object> classInfo) {
//		this.classInfo = classInfo;
//	}
//
//	public void setThreadInfos(List<Map<ThreadInfo, Object>> threadInfos) {
//		this.threadInfos = threadInfos;
//	}
//
//	public Map<OsInfo, Object> getOsInfo() {
//		return osInfo;
//	}
//
//	public Map<MemInfo, Object> getMemInfo() {
//		return memInfo;
//	}
//
//	public Map<ClassInfo, Object> getClassInfo() {
//		return classInfo;
//	}
//
//	public List<Map<ThreadInfo, Object>> getThreadInfos() {
//		return threadInfos;
//	}

}