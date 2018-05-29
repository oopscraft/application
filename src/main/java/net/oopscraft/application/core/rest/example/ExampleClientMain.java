package net.oopscraft.application.core.rest.example;

import org.apache.logging.log4j.core.config.Configurator;

import net.oopscraft.application.core.rest.RestClientMain;

public class ExampleClientMain {
	
	public static void main(String[] args) throws Exception {
		Configurator.initialize(null, "conf/log4j2.xml");
		String restRequestXml = "/" + ExampleClient.class.getPackage().getName().replaceAll("\\.", "/") + "/ExampleClient.xml";
		String host = "http://marketdata.krx.co.kr";
		new RestClientMain(ExampleClient.class, restRequestXml, host); 
	} 
	
}
