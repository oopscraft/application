package net.oopscraft.application.core.task;

public class TaskExecutorService {

	int threadPoolSize = 2;
	
	public TaskExecutorService(int threadPoolSize) {
		this.threadPoolSize = threadPoolSize;
	}

}
