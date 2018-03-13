package net.oopscraft.application.core.monitor;

import java.util.List;
import java.util.Observable;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.oopscraft.application.core.JmxInfo;
import net.oopscraft.application.core.JmxInfoFactory;



public class MonitorDaemon extends Observable implements Runnable {
	
	private static final Log LOG = LogFactory.getLog(MonitorDaemon.class);
	private static MonitorDaemon instance = null;
	private int intervalSeconds = 3;
	private int historySize = 10;

	private Thread thread = null;
	private List<JmxInfo> jmxInfoHistory = new CopyOnWriteArrayList<JmxInfo>();
	
	/**
	 * Initialize MonitorAgent
	 * @param intervalSeconds
	 * @param historySize
	 * @return
	 * @throws Exception
	 */
	public synchronized static MonitorDaemon getInstance() throws Exception {
		synchronized(MonitorDaemon.class) {
			if(instance == null) {
				instance = new MonitorDaemon();
			}
			return instance;
		}
	}
	
	private MonitorDaemon() throws Exception {
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

}
