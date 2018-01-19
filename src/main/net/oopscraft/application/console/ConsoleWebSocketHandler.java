package net.oopscraft.application.console;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import net.oopscraft.application.core.JsonConverter;
import net.oopscraft.application.core.ValueMap;
import net.oopscraft.application.core.WebSocketHandler;
import net.oopscraft.application.core.monitor.Monitor;
import net.oopscraft.application.core.monitor.MonitorAgent;
import net.oopscraft.application.core.monitor.MonitorAgentListener;


public class ConsoleWebSocketHandler extends WebSocketHandler {
	
	private static final Log LOG = LogFactory.getLog(ConsoleWebSocketHandler.class);
	
	enum Id { jmxInfoHistory, jmxInfo }

	@Override
	public void onCreate() {
		// Adds JMX Monitor Listener
		MonitorAgent.getInstance().addListener(new MonitorAgentListener() {
			@Override
			public void onCheck(Monitor jmxInfo, List<Monitor> jmxInfoHistory) throws Exception {
				ValueMap messageMap = new ValueMap();
				messageMap.set("id", Id.jmxInfo);
				messageMap.set("result", convertJmxInfoToMap(jmxInfo));				
				String message = JsonConverter.convertObjectToJson(messageMap);
				broadcastMessage(message);
			}
		});
	}

	@Override
	public void onDestroy() {
	}

	@Override
	public void onConnect(WebSocketSession session) {
	}

	@Override
	public void onClose(WebSocketSession session, CloseStatus status) {
	}

	@Override
	public void onMessage(WebSocketSession session, TextMessage message) {
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
		for(Monitor jmxInfo : MonitorAgent.getInstance().getJmxInfoHistory()) {
			list.add(convertJmxInfoToMap(jmxInfo));
		}
		return list;
	}
	
	private ValueMap getJmxInfoResult() throws Exception {
		return convertJmxInfoToMap(MonitorAgent.getInstance().getLastestJmxInfo());
	}
	
	private ValueMap convertJmxInfoToMap(Monitor jmxInfo) throws Exception {
		ValueMap jmxInfoMap = new ValueMap();
		jmxInfoMap.set("osInfo", jmxInfo.getOsInfo());
		jmxInfoMap.set("memInfo", jmxInfo.getMemInfo());
		jmxInfoMap.set("classInfo", jmxInfo.getClassInfo());
		jmxInfoMap.set("diskInfo", jmxInfo.getDiskInfo());
		jmxInfoMap.set("threadInfoList", jmxInfo.getThreadInfoList());
		return jmxInfoMap;
	}

}
