package net.oopscraft.application.core.process;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessExecutor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProcessExecutor.class);

	private ProcessBuilder processBuilder = new ProcessBuilder();
	private Process process;
	private ProcessStreamHandler processStreamHandler;
	
	public void setCommand(String command) throws Exception {
        if (command.length() == 0) {
        	throw new IllegalArgumentException("Empty command");
        }
        StringTokenizer st = new StringTokenizer(command);
        String[] cmdarray = new String[st.countTokens()];
        for (int i = 0; st.hasMoreTokens(); i++) {
            cmdarray[i] = st.nextToken();
        }
        processBuilder.command(cmdarray);
	}
	
	public void setCommand(String... commands) {
		processBuilder.command(commands);
	}

	public void setCommand(List<String> commands) {
		processBuilder.command(commands);
	}
	
	public void setProcessStreamHandler(ProcessStreamHandler processStreamHandler) {
		this.processStreamHandler = processStreamHandler;
	}

	public int execute() throws Exception {
		int exitValue = -1;
		Thread stdThread = null;
		Thread errThread = null;
		try {
			// starts process
			process = processBuilder.start();
	
			// standard out stream
			stdThread = createStreamReadThread(process.getInputStream(),processStreamHandler);
			stdThread.start();
			
			// error stream
			final StringBuffer errorMessage = new StringBuffer();
			errThread = createStreamReadThread(process.getErrorStream(),new ProcessStreamHandler() {
				@Override
				public void readLine(String line) {
					processStreamHandler.readLine(line);
					errorMessage.append(System.lineSeparator()).append(line);
				}
			});
			errThread.start();
			
			// wait and check exit value
			exitValue = process.waitFor();
			if(exitValue != 0) {
				throw new ProcessException(exitValue, errorMessage.toString());
			}
			return exitValue;
		}catch(Exception e) {
			throw e;
		}finally {
			if(stdThread != null) {
				try { stdThread.interrupt(); }catch(Exception ignore) {}
			}
			if(errThread != null) {
				try { errThread.interrupt(); }catch(Exception ignore) {}
			}
			if(process != null) {
				try { process.destroy(); }catch(Exception ignore) {}
			}
		}
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
	 * Destroys process.
	 */
	public void destroy() {
		if(process != null) {
			process.destroy();
		}
	}
	
	

}
