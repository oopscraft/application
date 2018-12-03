package net.oopscraft.application.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import net.oopscraft.application.core.JsonUtils;
import net.oopscraft.application.core.ValueMap;
import net.oopscraft.application.core.monitor.MonitorInfo;
import net.oopscraft.application.core.monitor.MonitorAgent;
import net.oopscraft.application.core.monitor.MonitorAgentListener;


public class WebSocketHandler extends net.oopscraft.application.core.spring.WebSocketHandler {
	
	private static final Log LOG = LogFactory.getLog(WebSocketHandler.class);
	enum Id { 
		 jmxInfoHistory 
		,jmxInfo 
	};
	private static BlockingQueue<String> messageQueue = null;
	Thread messageThread = null;
	
	/**
	 * sendMessage
	 * @param message
	 * @throws Exception
	 */
	public static void sendMessage(String message) throws Exception {
		messageQueue.put(message);
	}
	
	@Override
	public void onCreate() {
		LOG.info("onCreate");
		
		// Creates message queue and consumer thread.
		messageQueue = new LinkedBlockingQueue<String>();
		messageThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while(!Thread.interrupted()) {
					try {
						String message = messageQueue.take();
						broadcastMessage(message);
					}catch(Exception e) {
						LOG.warn(e.getMessage(), e);
					}
				}
			}
		});
		messageThread.setDaemon(true);
		messageThread.start();
		
		// creates JmxMonitor instance
		try {
			MonitorAgent.intialize(3, 10);
			MonitorAgent jmxMonitor = MonitorAgent.getInstance();
			jmxMonitor.addListener(new MonitorAgentListener() {
				@Override
				public void onCheck(MonitorInfo jmxInfo, List<MonitorInfo> jmxInfoHistory) throws Exception {
					ValueMap messageMap = new ValueMap();
					messageMap.set("id", Id.jmxInfo);
					messageMap.set("result", convertJmxInfoToMap(jmxInfo));				
					String message = JsonUtils.toJson(messageMap);
					broadcastMessage(message);
				}
				
			});
		}catch(Exception e) {
			LOG.warn(e.getMessage(), e);
		}
	}

	@Override
	public void onDestroy() {
		LOG.info("onDestroy");
		messageThread.interrupt();
	}

	@Override
	public void onConnect(WebSocketSession session) {
		LOG.info("onConnect");
	}

	@Override
	public void onClose(WebSocketSession session, CloseStatus status) {
		LOG.info("onClose");
	}

	@Override
	public void onMessage(WebSocketSession session, TextMessage message) {
		LOG.info("onMessage");
		try {
			ValueMap messageMap = JsonUtils.toObject(message.getPayload(), ValueMap.class);
			Id id = Id.valueOf(messageMap.getString("id"));
			Object result = null;
			switch(id) {
			case jmxInfoHistory :
				List<ValueMap> list = new ArrayList<ValueMap>();
				for(MonitorInfo jmxInfo : MonitorAgent.getInstance().getJmxInfoHistory()) {
					list.add(convertJmxInfoToMap(jmxInfo));
				}
				result = list;
			break;
			case jmxInfo :
				result = convertJmxInfoToMap(MonitorAgent.getInstance().getLastestJmxInfo());
			break;
			default :
			break;
			}
			messageMap.set("result", result);
			String response = JsonUtils.toJson(messageMap);
			this.sendMessage(session, response);
		}catch(Exception ignore) {
			LOG.warn(ignore.getMessage());
			this.sendMessage(session, ignore.getMessage());
		}
	}

	private ValueMap convertJmxInfoToMap(MonitorInfo jmxInfo) throws Exception {
		ValueMap jmxInfoMap = new ValueMap();
		jmxInfoMap.set("osInfo", jmxInfo.getOsInfo());
		jmxInfoMap.set("memInfo", jmxInfo.getMemInfo());
		jmxInfoMap.set("classInfo", jmxInfo.getClassInfo());
		jmxInfoMap.set("diskInfo", jmxInfo.getDiskInfo());
		jmxInfoMap.set("threadInfoList", jmxInfo.getThreadInfoList());
		return jmxInfoMap;
	}

}
