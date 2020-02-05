package net.oopscraft.application.util.monitor;

import java.util.Observable;
import java.util.Observer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class MonitorListener implements Observer {
	
	private static final Log LOG = LogFactory.getLog(MonitorListener.class);

	@Override
	public void update(Observable observable, Object arg1) {
		try {
			MonitorAgent monitorAgent = (MonitorAgent) observable;
			onCheck(monitorAgent.getMonitorInfo());
		}catch(Exception e) {
			LOG.warn(e.getMessage(),e);
		}
	}

	/**
	 * Check 
	 * @throws Exception
	 */
	public abstract void onCheck(MonitorInfo monitorInfo) throws Exception;

}
