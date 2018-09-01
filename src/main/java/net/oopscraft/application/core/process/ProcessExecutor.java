package net.oopscraft.application.core.process;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessExecutor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProcessExecutor.class);

	private Process process;
	
	/**
	 * Executes command as process.
	 * @param command
	 * @param processStreamHandler
	 * @return	
	 * @throws Exception
	 */
	public int execute(List<String> command, final ProcessStreamHandler processStreamHandler) throws Exception {
		
		ProcessBuilder processBuilder = new ProcessBuilder();
		processBuilder.command(command);
		process = processBuilder.start();

		// standard out stream
		Thread stdThread = createStreamReadThread(process.getInputStream(),processStreamHandler);
		stdThread.start();
		
		// error stream
		final StringBuffer errorMessage = new StringBuffer();
		Thread errThread = createStreamReadThread(process.getErrorStream(),new ProcessStreamHandler() {
			@Override
			public void readLine(String line) {
				processStreamHandler.readLine(line);
				errorMessage.append(System.lineSeparator()).append(line);
			}
		});
		errThread.start();
		
		// wait and check exit value
		int exitValue = process.waitFor();
		if(exitValue != 0) {
			throw new ProcessException(exitValue, errorMessage.toString());
		}
		return exitValue;
	}
	
	/**
	 * Creates thread for reading stream.
	 * @param inputStream
	 * @param processStreamHandler
	 * @return
	 * @throws Exception
	 */
	private Thread createStreamReadThread(final InputStream inputStream, final ProcessStreamHandler processStreamHandler) throws Exception {
		Thread streamReadThread = new Thread(new Runnable() {
			@Override
			public void run() {
				InputStreamReader isr = null;
				BufferedReader br = null;
				try {
					isr = new InputStreamReader(inputStream, Charset.defaultCharset());
					br = new BufferedReader(isr);
					String line;
					while((line = br.readLine()) != null) {
						LOGGER.debug(line);
						processStreamHandler.readLine(line);
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
	
	/**
	 * Destroies process.
	 */
	public void destroy() {
		if(process != null) {
			process.destroy();
		}
	}
	
	

}
