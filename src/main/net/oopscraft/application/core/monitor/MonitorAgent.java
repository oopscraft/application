package net.oopscraft.application.core.monitor;

import java.util.List;
import java.util.Observable;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.oopscraft.application.core.JmxInfo;
import net.oopscraft.application.core.JmxInfoFactory;



public class MonitorAgent extends Observable implements Runnable {
	
	private static final Log LOG = LogFactory.getLog(MonitorAgent.class);
	private static MonitorAgent instance = null;

	private Thread thread = null;
	private int intervalSeconds = 3;
	private int historySize = 10;
	private List<JmxInfo> jmxInfoHistory = new CopyOnWriteArrayList<JmxInfo>();
	
	/**
	 * Initialize MonitorAgent
	 * @param intervalSeconds
	 * @param historySize
	 * @return
	 * @throws Exception
	 */
	public synchronized static MonitorAgent intialize(int intervalSeconds, int historySize) throws Exception {
		synchronized(MonitorAgent.class) {
			if(instance == null) {
				instance = new MonitorAgent(intervalSeconds, historySize);
			}
			return instance;
		}
	}
	
	/**
	 * getInstance
	 * @return
	 */
	public static MonitorAgent getInstance() throws Exception {
		synchronized(MonitorAgent.class) {
			if(instance == null) {
				throw new Exception("Monitor instance is not initialized."); 
			}
			return instance;
		}
	}
	
	private MonitorAgent(int interval, int limit) throws Exception {
		this.intervalSeconds = interval;
		this.historySize = limit;
		thread = new Thread(this);
		thread.setDaemon(true);
		thread.setPriority(Thread.MAX_PRIORITY);
		thread.start();
	}
	
	@Override
	public void run() {
		while(!Thread.interrupted()) {
			try {
				JmxInfo jmxInfo = JmxInfoFactory.getJmxInfo();
				jmxInfoHistory.add(jmxInfo);
				if(jmxInfoHistory.size() > historySize) {
					jmxInfoHistory.remove(0);
				}
				
				// notify
				setChanged();
				notifyObservers();
				
			}catch(Exception e) {
				LOG.warn(e.getMessage(), e);
			}finally {
				try { Thread.sleep(1000 * intervalSeconds); }catch(Exception ignore) {}
			}
		}
		
	}
	
	public List<JmxInfo> getJmxInfoHistory() {
		return jmxInfoHistory;
	}
	
	public JmxInfo getLastestJmxInfo() {
		return jmxInfoHistory.get(jmxInfoHistory.size() -1);
	}
	
	public void addListener(MonitorAgentListener listener) {
		addObserver(listener);
	}
	
	public void removeListener(MonitorAgentListener listener) {
		removeListener(listener);
	}

}
