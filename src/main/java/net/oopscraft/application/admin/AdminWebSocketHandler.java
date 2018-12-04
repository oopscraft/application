package net.oopscraft.application.admin;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import net.oopscraft.application.core.JsonUtils;
import net.oopscraft.application.core.ValueMap;
import net.oopscraft.application.core.monitor.MonitorAgent;
import net.oopscraft.application.core.monitor.MonitorInfo;
import net.oopscraft.application.core.monitor.MonitorListener;

public class AdminWebSocketHandler extends net.oopscraft.application.core.spring.WebSocketHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(AdminWebSocketHandler.class);

	public enum MessageId {
		monitorInfo;
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
				public void onCheck(List<MonitorInfo> monitorInfoList) throws Exception {
					String message = createMessage(MessageId.monitorInfo, monitorInfoList);
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
	 * @param messageId
	 * @param message
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
			switch(messageId){
			case monitorInfo :
				String responseMessage = createMessage(messageId, monitorAgent.getMonitorInfoList());
				this.sendMessage(session, responseMessage);
			break;
			}
		} catch (Exception ignore) {
			LOGGER.warn(ignore.getMessage());
			sendMessage(session, ignore.getMessage());
		}
	}

}
