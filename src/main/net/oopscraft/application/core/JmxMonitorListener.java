package net.oopscraft.application.core;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class JmxMonitorListener implements Observer {
	
	private static final Log LOG = LogFactory.getLog(JmxMonitorListener.class);

	@Override
	public void update(Observable observable, Object arg1) {
		try {
			JmxMonitor jmxMonitor = (JmxMonitor) observable;
			onCheck(jmxMonitor.getLastestJmxInfo(), jmxMonitor.getJmxInfoHistory());
		}catch(Exception e) {
			LOG.warn(e.getMessage(),e);
		}
	}

	/**
	 * Check 
	 * @throws Exception
	 */
	public abstract void onCheck(JmxInfo jmxInfo, List<JmxInfo> jmxInfoHistory) throws Exception;

}
