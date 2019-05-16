package net.oopscraft.application.core.task;

import java.util.concurrent.Callable;

import net.oopscraft.application.core.ValueMap;

public abstract class Task<T> implements Callable<T>{
	
	Object object;
	
	public Task(Object object) {
		this.object = object;
	}
	
	public Object getObject() {
		return this.object;
	}
	
	
	public abstract T call() throws Exception;

}
