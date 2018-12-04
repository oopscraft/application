package net.oopscraft.application.monitor;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.oopscraft.application.monitor.MonitorInfo;

public abstract class MonitorListener implements Observer {
	
	private static final Log LOG = LogFactory.getLog(MonitorListener.class);

	@Override
	public void update(Observable observable, Object arg1) {
		try {
			MonitorAgent monitorAgent = (MonitorAgent) observable;
			onCheck(monitorAgent.getMonitorInfoList());
		}catch(Exception e) {
			LOG.warn(e.getMessage(),e);
		}
	}

	/**
	 * Check 
	 * @throws Exception
	 */
	public abstract void onCheck(List<MonitorInfo> monitorInfoList) throws Exception;

}
