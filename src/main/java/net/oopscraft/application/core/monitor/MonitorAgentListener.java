package net.oopscraft.application.core.monitor;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.oopscraft.application.core.monitor.MonitorInfo;

public abstract class MonitorAgentListener implements Observer {
	
	private static final Log LOG = LogFactory.getLog(MonitorAgentListener.class);

	@Override
	public void update(Observable observable, Object arg1) {
		try {
			MonitorAgent jmxMonitor = (MonitorAgent) observable;
			onCheck(jmxMonitor.getLastestJmxInfo(), jmxMonitor.getJmxInfoHistory());
		}catch(Exception e) {
			LOG.warn(e.getMessage(),e);
		}
	}

	/**
	 * Check 
	 * @throws Exception
	 */
	public abstract void onCheck(MonitorInfo jmxInfo, List<MonitorInfo> jmxInfoHistory) throws Exception;

}
