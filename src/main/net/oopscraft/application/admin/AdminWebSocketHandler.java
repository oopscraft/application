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

import net.oopscraft.application.core.JmxInfo;
import net.oopscraft.application.core.JmxMonitor;
import net.oopscraft.application.core.JmxMonitorListener;
import net.oopscraft.application.core.JsonConverter;
import net.oopscraft.application.core.ValueMap;
import net.oopscraft.application.core.WebSocketHandler;


public class AdminWebSocketHandler extends WebSocketHandler {
	
	private static final Log LOG = LogFactory.getLog(AdminWebSocketHandler.class);
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
			JmxMonitor.intialize(3, 10);
			JmxMonitor jmxMonitor = JmxMonitor.getInstance();
			jmxMonitor.addListener(new JmxMonitorListener() {
				@Override
				public void onCheck(JmxInfo jmxInfo, List<JmxInfo> jmxInfoHistory) throws Exception {
					ValueMap messageMap = new ValueMap();
					messageMap.set("id", Id.jmxInfo);
					messageMap.set("result", convertJmxInfoToMap(jmxInfo));				
					String message = JsonConverter.convertObjectToJson(messageMap);
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
			ValueMap messageMap = JsonConverter.convertJsonToObject(message.getPayload(), ValueMap.class);
			Id id = Id.valueOf(messageMap.getString("id"));
			Object result = null;
			switch(id) {
			case jmxInfoHistory :
				List<ValueMap> list = new ArrayList<ValueMap>();
				for(JmxInfo jmxInfo : JmxMonitor.getInstance().getJmxInfoHistory()) {
					list.add(convertJmxInfoToMap(jmxInfo));
				}
				result = list;
			break;
			case jmxInfo :
				result = convertJmxInfoToMap(JmxMonitor.getInstance().getLastestJmxInfo());
			break;
			default :
			break;
			}
			messageMap.set("result", result);
			String response = JsonConverter.convertObjectToJson(messageMap);
			this.sendMessage(session, response);
		}catch(Exception ignore) {
			LOG.warn(ignore.getMessage());
			this.sendMessage(session, ignore.getMessage());
		}
	}

	private ValueMap convertJmxInfoToMap(JmxInfo jmxInfo) throws Exception {
		ValueMap jmxInfoMap = new ValueMap();
		jmxInfoMap.set("osInfo", jmxInfo.getOsInfo());
		jmxInfoMap.set("memInfo", jmxInfo.getMemInfo());
		jmxInfoMap.set("classInfo", jmxInfo.getClassInfo());
		jmxInfoMap.set("diskInfo", jmxInfo.getDiskInfo());
		jmxInfoMap.set("threadInfoList", jmxInfo.getThreadInfoList());
		return jmxInfoMap;
	}

}
