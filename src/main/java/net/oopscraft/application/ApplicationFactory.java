package net.oopscraft.application;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import net.oopscraft.application.core.PasswordBasedEncryptor;
import net.oopscraft.application.core.WebServer;
import net.oopscraft.application.core.WebServerContext;
import net.oopscraft.application.core.XPathReader;

public class ApplicationFactory {
	
	private static final String PROPERTY_IDENTIFIER = "\\$\\{(.*?)\\}";
	
	static Application getApplication(Class<?> clazz, File contextXmlFile, File contextPropertiesFile) throws Exception {
		
		Application application = (Application) clazz.newInstance();
		application.setContextXmlFile(contextXmlFile);
		application.setContextPropertiesFile(contextPropertiesFile);

		// creates resources.
		XPathReader contextXmlReader = new XPathReader(contextXmlFile);
		Properties contextProperties = new Properties();
		contextProperties.load(new FileInputStream(contextPropertiesFile));
		application.setWebServers(createWebServers(contextXmlReader, contextProperties));
		application.setDataSources(createDataSources(contextXmlReader, contextProperties));
		
		return application;
	}

	/**
	 * parseValue
	 * @param value
	 * @param properties
	 * @return
	 * @throws Exception
	 */
	private static String parseValue(String value, Properties properties) throws Exception {
		Pattern p = Pattern.compile(PROPERTY_IDENTIFIER);
        Matcher m = p.matcher(value);
        StringBuffer sb = new StringBuffer();
        while(m.find()) {
            String propertyName = m.group(1);
            String propertyValue = StringUtils.defaultString(properties.getProperty(propertyName),propertyName);
            String decryptedValue = PasswordBasedEncryptor.decryptIdentifiedValue(propertyValue);
            m.appendReplacement(sb, Matcher.quoteReplacement(decryptedValue));
        }
        m.appendTail(sb);
       return sb.toString();
	}

	/**
	 * Creates Web Server instance.
	 * @param context
	 * @throws Exception
	 */
	private static HashMap<String,WebServer> createWebServers(XPathReader contextXmlReader, Properties contextProperties) throws Exception {
		HashMap<String,WebServer> webServers = new LinkedHashMap<String,WebServer>();
		NodeList webServerNodeList = (NodeList) contextXmlReader.getElement("/application/webServer");
		for(int idx = 1; idx <= webServerNodeList.getLength(); idx ++ ) {
			String webServerExpression = String.format("/application/webServer[%d]", idx);
			String id = contextXmlReader.getTextContent(webServerExpression + "/@id");
			int port = Integer.parseInt(contextXmlReader.getTextContent(webServerExpression + "/@port"));
			WebServer webServer = new WebServer();
			webServer.setPort(port);
			
			// setting SSL properties
			boolean ssl = Boolean.parseBoolean(contextXmlReader.getTextContent(webServerExpression + "/ssl"));
			if(ssl == true) {
				webServer.setSsl(true);
				String keyStorePath = contextXmlReader.getTextContent(webServerExpression + "/ssl/keyStorePath");
				String keyStoreType = contextXmlReader.getTextContent(webServerExpression + "/ssl/keyStoreType");
				String keyStorePass = contextXmlReader.getTextContent(webServerExpression + "/ssl/keyStorePass");
				webServer.setKeyStorePath(keyStorePath);
				webServer.setKeyStoreType(keyStoreType);
				webServer.setKeyStorePass(parseValue(keyStorePass, contextProperties));
			}
			
			// adds context
			NodeList contextNodeList = (NodeList) contextXmlReader.getElement(webServerExpression + "/context");
			for(int i = 1; i <= contextNodeList.getLength(); i ++) {
				String contextExpression = String.format(webServerExpression + "/context[%d]", i);
				String path = contextXmlReader.getTextContent(contextExpression + "/@path");
				String resourceBase = contextXmlReader.getTextContent(contextExpression + "/resourceBase");
				String descriptor = contextXmlReader.getTextContent(contextExpression + "/descriptor");
				WebServerContext webServerContext = new WebServerContext();
				webServerContext.setContextPath(path);
				webServerContext.setResourceBase(resourceBase);
				webServerContext.setDescriptor(descriptor);
				webServer.addContext(webServerContext);
			}
			
			// add webServer
			webServers.put(id, webServer);
		}
		return webServers;
	}
	
	/**
	 * initializeDataSource
	 * @param configXmlReader
	 * @throws Exception
	 */
	private static HashMap<String,DataSource> createDataSources(XPathReader contextXmlReader, Properties contextProperties) throws Exception {
		HashMap<String,DataSource> dataSources = new LinkedHashMap<String,DataSource>();
		NodeList dataSourceNodeList = (NodeList) contextXmlReader.getElement("/application/dataSource");
		for(int idx = 1; idx <= dataSourceNodeList.getLength(); idx ++ ) {
			String dataSourceExpression = String.format("/application/dataSource[%d]", idx);
			String id = contextXmlReader.getTextContent(dataSourceExpression + "/@id");
			
			// parses dataSource properties
			Properties datasourceProperties = new Properties();
			NodeList propertiesNodeList = (NodeList) contextXmlReader.getElement(dataSourceExpression + "/*");
			for(int i = 0; i < propertiesNodeList.getLength(); i ++) {
				Node propertyNode = propertiesNodeList.item(i);
				String name = propertyNode.getNodeName();
				String value = propertyNode.getTextContent();
				datasourceProperties.setProperty(name, parseValue(StringUtils.defaultString(value), contextProperties));
			}
			
			// creates dastaSource instance.
			DataSource dataSource = BasicDataSourceFactory.createDataSource(datasourceProperties);
			dataSources.put(id, dataSource);
		}
		return dataSources;
	}
	
}
