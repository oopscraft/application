package net.oopscraft.application.core.spring;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;


public abstract class WebSocketHandler extends TextWebSocketHandler {
	
	private static final Log LOG = LogFactory.getLog(WebSocketHandler.class);
	
	private List<WebSocketSession> sessionList = new CopyOnWriteArrayList<WebSocketSession>();
	
	public WebSocketHandler() {
		super();
		onCreate();
	}

	public void finalize() throws Throwable { 
		try { 
			onDestroy();
		}catch(Exception ignore){ 
			LOG.warn(ignore.getMessage(), ignore);
		}
	} 
	
	@Override
	public final void afterConnectionEstablished(WebSocketSession session) throws Exception {
		LOG.debug(String.format("afterConnectionEstablished(%s)", session));
		sessionList.add(session);
		onConnect(session);
	}
	
	@Override
	public final void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		LOG.debug(String.format("afterConnectionClosed(%s,%s)", session, status));
		sessionList.remove(session);
		onClose(session, status);
	}

	@Override
	protected final void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		onMessage(session, message);
	}
	
	/**
	 * sendMessage
	 * @param session
	 * @param message
	 */
	protected final void sendMessage(WebSocketSession session, String message) {
		try {
			TextMessage textMessage = new TextMessage(message);
			session.sendMessage(textMessage);
		}catch(Exception ignore) {
			LOG.warn(ignore.getMessage());
		}
	}
	
	/**
	 * broadcastMessage
	 * @param message
	 * @throws Exception
	 */
	protected final void broadcastMessage(String message) {
		TextMessage textMessage = new TextMessage(message);
		for(WebSocketSession session : sessionList) {
			try {
				session.sendMessage(textMessage);
			}catch(Exception ignore) {
				LOG.warn(ignore.getMessage());
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
