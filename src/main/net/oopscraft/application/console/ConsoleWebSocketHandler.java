package net.oopscraft.application.console;

import java.util.ArrayList;
import java.util.List;

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


public class ConsoleWebSocketHandler extends WebSocketHandler {
	
	private static final Log LOG = LogFactory.getLog(ConsoleWebSocketHandler.class);
	
	enum Id { jmxInfoHistory, jmxInfo }

	@Override
	public void onCreate() {
		// Adds JMX Monitor Listener
		JmxMonitor.getInstance().addListener(new JmxMonitorListener() {
			@Override
			public void onCheck(JmxInfo jmxInfo, List<JmxInfo> jmxInfoHistory) throws Exception {
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
		}catch(Exception ignore) {
			LOG.warn(ignore.getMessage());
		}
	}
	
	private Object getJmxInfoHistoryResult() throws Exception {
		List<ValueMap> list = new ArrayList<ValueMap>();
		for(JmxInfo jmxInfo : JmxMonitor.getInstance().getJmxInfoHistory()) {
			list.add(convertJmxInfoToMap(jmxInfo));
		}
		return list;
	}
	
	private ValueMap getJmxInfoResult() throws Exception {
		return convertJmxInfoToMap(JmxMonitor.getInstance().getLastestJmxInfo());
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
