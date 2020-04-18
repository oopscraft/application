package net.oopscraft.application.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import net.oopscraft.application.core.JsonConverter;
import net.oopscraft.application.core.spring.BroadcastWebSocketHandler;
import net.oopscraft.application.monitor.MonitorService;

@Component
public class WebSocketHandler extends BroadcastWebSocketHandler {
	
	@Autowired
	MonitorService monitorService;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnect(WebSocketSession session) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClose(WebSocketSession session, CloseStatus status) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMessage(WebSocketSession session, TextMessage message) {
		try {
			session.sendMessage(new TextMessage("######" + JsonConverter.toJson(monitorService.getMonitors())));
		}catch(Exception e) {
			e.printStackTrace(System.err);
		}
	}

}
