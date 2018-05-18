package net.oopscraft.application.core;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.oopscraft.application.core.SystemInfo;

public abstract class SystemMonitorListener implements Observer {
	
	private static final Log LOG = LogFactory.getLog(SystemMonitorListener.class);

	@Override
	public void update(Observable observable, Object arg1) {
		try {
			SystemMonitor jmxMonitor = (SystemMonitor) observable;
			onCheck(jmxMonitor.getLastestJmxInfo(), jmxMonitor.getJmxInfoHistory());
		}catch(Exception e) {
			LOG.warn(e.getMessage(),e);
		}
	}

	/**
	 * Check 
	 * @throws Exception
	 */
	public abstract void onCheck(SystemInfo jmxInfo, List<SystemInfo> jmxInfoHistory) throws Exception;

}
