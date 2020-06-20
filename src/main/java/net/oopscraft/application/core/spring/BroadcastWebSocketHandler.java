package net.oopscraft.application.core.spring;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;


public abstract class BroadcastWebSocketHandler extends TextWebSocketHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BroadcastWebSocketHandler.class);
	
	private List<WebSocketSession> sessionList = new CopyOnWriteArrayList<WebSocketSession>();
	
	public BroadcastWebSocketHandler() {
		super();
		onCreate();
	}

	public void finalize() throws Throwable { 
		try { 
			onDestroy();
		}catch(Exception ignore){ 
			LOGGER.warn(ignore.getMessage(), ignore);
		}
	} 
	
	@Override
	public final void afterConnectionEstablished(WebSocketSession session) throws Exception {
		LOGGER.debug(String.format("afterConnectionEstablished(%s)", session));
		sessionList.add(session);
		onConnect(session);
	}
	
	@Override
	public final void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		LOGGER.debug(String.format("afterConnectionClosed(%s,%s)", session, status));
		sessionList.remove(session);
		onClose(session, status);
	}

	@Override
	protected final void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		onMessage(session, message);
	}
	
	/**
	 * broadcastMessage
	 * @param message
	 * @throws Exception
	 */
	public final void broadcastMessage(String message) {
		TextMessage textMessage = new TextMessage(message);
		for(WebSocketSession session : sessionList) {
			try {
				session.sendMessage(textMessage);
			}catch(Exception ignore) {
				LOGGER.warn(ignore.getMessage());
			}
		}
	}

	/**
	 * onCreate
	 */
	public abstract void onCreate();
	
	/**
	 * onDestroy
	 */
	public abstract void onDestroy();
	
	/**
	 * onConnect
	 * @param session
	 */
	public abstract void onConnect(WebSocketSession session);
	
	/**
	 * onClose
	 * @param session
	 * @param status
	 */
	public abstract void onClose(WebSocketSession session, CloseStatus status);
	
	/**
	 * onMessage
	 * @param session
	 * @param message
	 */
	public abstract void onMessage(WebSocketSession session, TextMessage message);

}
