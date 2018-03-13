package net.oopscraft.application.console;

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
import net.oopscraft.application.core.JsonConverter;
import net.oopscraft.application.core.ValueMap;
import net.oopscraft.application.core.WebSocketHandler;
import net.oopscraft.application.core.monitor.MonitorDaemon;


public class ConsoleWebSocketHandler extends WebSocketHandler {
	
	private static final Log LOG = LogFactory.getLog(ConsoleWebSocketHandler.class);
	
	enum Id { jmxInfoHistory, jmxInfo }
	
	private static BlockingQueue<String> messageQueue = new LinkedBlockingQueue<String>();
	Thread messageThread = new Thread(new Runnable() {
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
		messageThread.setDaemon(true);
		messageThread.start();
		
		// creates monitorDaemon instance
		try {
			MonitorDaemon.getInstance();
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
		this.sendMessage(session, message.getPayload());
		try {
			ValueMap messageMap = JsonConverter.convertJsonToObject(message.getPayload(), ValueMap.class);
			Id id = Id.valueOf(messageMap.getString("id"));
			switch(id) {
			case jmxInfoHistory :
				messageMap.set("result", getJmxInfoHistoryResult());
			break;
			case jmxInfo :
				messageMap.set("result", getJmxInfoResult());
			break;
			default :
			break;
			}
			String response = JsonConverter.convertObjectToJson(messageMap);
			this.sendMessage(session, response);
		}catch(Exception ignore) {
			LOG.warn(ignore.getMessage());
			this.sendMessage(session, ignore.getMessage());
		}
	}
	
	private Object getJmxInfoHistoryResult() throws Exception {
		List<ValueMap> list = new ArrayList<ValueMap>();
		for(JmxInfo jmxInfo : MonitorDaemon.getInstance().getJmxInfoHistory()) {
			list.add(convertJmxInfoToMap(jmxInfo));
		}
		return list;
	}
	
	private ValueMap getJmxInfoResult() throws Exception {
		return convertJmxInfoToMap(MonitorDaemon.getInstance().getLastestJmxInfo());
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
