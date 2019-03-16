package net.oopscraft.application.core.process;

import org.junit.Test;

public class ProcessExecutorTest {
	
	@Test
	public void test() throws Exception {
		final StringBuffer buffer = new StringBuffer();
	    ProcessExecutor processExecutor = new ProcessExecutor();
	    try {
		    String osName = System.getProperty("os.name").toLowerCase();
		    if(osName.contains("win")) {
			    processExecutor.setCommand("cmd /C tasklist /FI \"STATUS eq running\" /V | sort /r /+65");
		    }else{
		    	//processExecutor.setCommand("top -b -n1 -c");
		    	processExecutor.setCommand(new String[] {
		    		"/bin/sh"
		    		,"-c"
		    		,"ps aux --sort -%cpu,%mem | head -30" 
		    	});
		    }
		    processExecutor.setProcessStreamHandler(new ProcessStreamHandler() {
				@Override
				public void readLine(String line) {
					System.out.println(line);
					buffer.append(line).append(System.lineSeparator());
				}
		    });
		    processExecutor.execute();
	    }catch(Exception e) {
	    	e.printStackTrace(System.err);
	    }finally {
	    	if(processExecutor != null) {
	    		try {
	    			processExecutor.destroy();
	    		}catch(Exception ignore) {}
	    	}
	    }
		assert(true);
	}

}
