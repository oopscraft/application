package net.oopscraft.application.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import net.oopscraft.application.core.JsonUtils;
import net.oopscraft.application.core.ValueMap;
import net.oopscraft.application.monitor.MonitorAgent;
import net.oopscraft.application.monitor.MonitorInfo;
import net.oopscraft.application.monitor.MonitorListener;

public class AdminWebSocketHandler extends net.oopscraft.application.core.spring.WebSocketHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(AdminWebSocketHandler.class);

	public enum MessageId {
		 monitorInfos
		,monitorInfo
	}

	MonitorAgent monitorAgent;
	MonitorListener monitorListener;

	@Override
	public void onCreate() {
		LOGGER.info("onCreate");

		// Adds MonitorListener
		try {
			monitorAgent = MonitorAgent.getInstance();
			monitorListener = new MonitorListener() {
				@Override
				public void onCheck(MonitorInfo monitorInfo) throws Exception {
					String message = createMessage(MessageId.monitorInfo, monitorInfo);
					broadcastMessage(message);
				}
			};
			monitorAgent.addListener(monitorListener);
		} catch (Exception e) {
			LOGGER.warn(e.getMessage(), e);
		}
	}
	
	/**
	 * Creates message
	 * @param messageId message id
	 * @param message message value
	 * @return
	 * @throws Exception
	 */
	public String createMessage(MessageId messageId, Object message) throws Exception {
		ValueMap messageMap = new ValueMap();
		messageMap.put("id", messageId);
		messageMap.put("message", message);
		return JsonUtils.toJson(messageMap);
	}


	@Override
	public void onDestroy() {
		LOGGER.info("onDestroy");

		// Removes MonitorListener
		monitorAgent.removeListener(monitorListener);
	}

	@Override
	public void onConnect(WebSocketSession session) {
		LOGGER.info("onConnect");
	}

	@Override
	public void onClose(WebSocketSession session, CloseStatus status) {
		LOGGER.info("onClose");
	}

	@Override
	public void onMessage(WebSocketSession session, TextMessage message) {
		LOGGER.info("onMessage");
		try {
			ValueMap messageMap = JsonUtils.toObject(message.getPayload(), ValueMap.class);
			MessageId messageId = MessageId.valueOf(messageMap.getString("id"));
			String responseMessage = null;
			switch(messageId){
			case monitorInfos :
				responseMessage = createMessage(messageId, monitorAgent.getMonitorInfos());
			break;
			case monitorInfo : 
				responseMessage = createMessage(messageId, monitorAgent.getMonitorInfo());
			break;
			}
			this.sendMessage(session, responseMessage);
		} catch (Exception ignore) {
			LOGGER.warn(ignore.getMessage());
			sendMessage(session, ignore.getMessage());
		}
	}

}
