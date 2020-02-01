package net.oopscraft.application;

import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import net.oopscraft.application.admin.bak.AdminWebSocketHandler;

/**
 * Configuration of web socket handlers
 * in case of XML
 * <pre>
 *	<beans:bean id="adminWebSocketHandler" class="net.oopscraft.application.admin.AdminWebSocketHandler"/>	
 *	<websocket:handlers>
 *		<websocket:mapping path="admin/admin.ws" handler="adminWebSocketHandler"/>
 *	</websocket:handlers>
 * </pre>
 * @author chomookun@gmail.com
 *
 */
@EnableWebSocket
public class ApplicationWebSocketContext implements WebSocketConfigurer {

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(adminWebSocketHandler(), "admin/admin.ws");
	}
	
	@Bean
	public AdminWebSocketHandler adminWebSocketHandler() {
		AdminWebSocketHandler adminWebSocketHandler = new AdminWebSocketHandler();
		return adminWebSocketHandler;
	}

}
