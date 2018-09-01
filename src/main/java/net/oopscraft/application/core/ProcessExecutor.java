package net.lotte.chamomile.batch.core;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessExecutor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProcessExecutor.class);

	private Process process;
	
	public interface StreamHandler {
		public void readLine(String line);
	}
	
	public int execute(List<String> command, StreamHandler streamHandler) throws Exception, ProcessExecutorException {
		
		ProcessBuilder processBuilder = new ProcessBuilder();
		processBuilder.command(command);
		process = processBuilder.start();

		// standard out stream
		Thread stdThread = createStreamReadThread(process.getInputStream(),streamHandler);
		stdThread.start();
		
		// error stream
		final StringBuffer errorMessage = new StringBuffer();
		Thread errThread = createStreamReadThread(process.getErrorStream(),new StreamHandler() {
			@Override
			public void readLine(String line) {
				errorMessage.append(System.lineSeparator()).append(line);
			}
		});
		errThread.start();
		
		// wait and check exit value
		int exitValue = process.waitFor();
		if(exitValue != 0) {
			throw new ProcessExecutorException(errorMessage.toString());
		}
		return exitValue;
	}
	
	private Thread createStreamReadThread(final InputStream inputStream, final StreamHandler streamHandler) throws Exception {
		Thread streamReadThread = new Thread(new Runnable() {
			@Override
			public void run() {
				InputStreamReader isr = null;
				BufferedReader br = null;
				try {
					isr = new InputStreamReader(inputStream, "euc-kr");
					br = new BufferedReader(isr);
					String line;
					while((line = br.readLine()) != null) {
						LOGGER.debug(line);
						streamHandler.readLine(line);
					}
				}catch(Exception e) {
					LOGGER.error(e.getMessage(), e);
				}finally {
					if(br != null) {
						try {
							br.close();
						}catch(Exception ignore) {
							LOGGER.warn(ignore.getMessage());
						}
					}
					if(isr != null) {
						try {
							isr.close();
						}catch(Exception ignore) {
							LOGGER.warn(ignore.getMessage());
						}
					}
					if(inputStream != null) {
						try {
							inputStream.close();
						}catch(Exception ignore) {
							LOGGER.warn(ignore.getMessage());
						}
					}
				}
			}
		});
		return streamReadThread;
	}
	
	public void destroy() {
		process.destroy();
	}
	
	

}
