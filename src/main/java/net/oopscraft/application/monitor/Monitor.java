package net.oopscraft.application.monitor;

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
	
	public enum ThreadInfoKey {
		threadId,
		threadName,
		threadState,
		waitedCount, 
		waitedTime,
		blockCount,
		blockTime;
	}
	
	public enum ClassLoadingKey {
		totalLoadedClassCount,
		loadedClassCount,
		unloadedClassCount;
	}
	
	String id = this.getClass().getSimpleName();
	Date date;
	String top;
	ValueMap operatingSystem;
	ValueMap memory;
	ValueMap classLoading;
	List<ValueMap> threadInfos;
	List<ValueMap> beans;
	
	public Monitor(Date date) {
		this.date = date;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

}