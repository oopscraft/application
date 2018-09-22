package net.oopscraft.application;

import net.oopscraft.application.core.WebServer;

public class Application {
	
	WebServer webServer;
	
	public WebServer getWebServer() {
		return webServer;
	}
	public void setWebServer(WebServer webServer) {
		this.webServer = webServer;
	}
	
	/**
	 * process on application start
	 * @throws Exception
	 */
	public void onStart() throws Exception {
		// void
	};
	
	/**
	 * process on application stop
	 * @throws Exception
	 */
	public void onStop() throws Exception {
		// void
	};

}
