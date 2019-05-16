package net.oopscraft.application.core.task;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import net.oopscraft.application.core.ValueMap;

public class TaskExecutorServiceTest {
	
	ExecutorService executorService = Executors.newFixedThreadPool(2);
	ExecutorCompletionService<ValueMap> executorCompletionService = new ExecutorCompletionService<ValueMap>(executorService);
	
	
	@Test
	public void test() throws Exception {
		
		for(int i = 0; i < 10; i++) {
			ValueMap input = new ValueMap();
			input.set("value", i);
			Task task = new Task(input) {
	
				@Override
				public Object call() throws Exception {
					ValueMap object = (ValueMap) this.getObject();
					
					int random = (int) (Math.random()*10+1);
					Thread.sleep(1000 * random);
					System.err.println(object.get("value") + " is done!" + "random:" + random);
					
					ValueMap output = new ValueMap();
					output.setString("value", object.get("value"));
					return output;
				}
				
			};
			
			executorCompletionService.submit(task);
		}
		
		executorService.shutdown();
		while (executorService.isTerminated() == false) {
			Future<ValueMap> future = executorCompletionService.take();
			ValueMap output = future.get();
			System.out.println(output.get("value"));
 		}
			
	
		
		
		
	}

}
