package net.oopscraft.application.core;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.NodeList; 

public class HttpRequestFactory { 
	private static Logger logger = Logger.getLogger(HttpRequestFactory.class); 
	private static Map<String,HttpRequestFactory> instanceMap = new LinkedHashMap<String,HttpRequestFactory>(); 
	List<HttpRequest> requestList = new ArrayList<HttpRequest>(); 
	
	/** 
	 * Getting Context Manager (Singleton) 
	 * @return 
	 * @throws Exception 
	 */ 
	public synchronized static HttpRequestFactory getInstance(String resourceXml) throws Exception { 
		synchronized(HttpRequestFactory.class) { 
			if(!instanceMap.containsKey(resourceXml)) { 
				HttpRequestFactory instance = new HttpRequestFactory(resourceXml); 
				instanceMap.put(resourceXml, instance); 
			} 
			return instanceMap.get(resourceXml); 
		} 
	} 
	
	/** 
	 * Constructor 
	 * @throws Exception 
	 */ 
	private HttpRequestFactory(String resourceXml) throws Exception { 
		logger.info("Creating OpenApiContextManager Singleton Instance."); 
		// loading input stream from XML file in class path.
		XPathReader xmlReader = null;
		InputStream is = null; 
		try { 
			is = this.getClass().getResourceAsStream(resourceXml);
			xmlReader = new XPathReader(is);
			NodeList nodeList = (NodeList) xmlReader.getElement("//request");
			for(int idx = 0; idx < nodeList.getLength(); idx ++ ) { 
				String requestPath = String.format("//request[%d]",idx + 1);
				String id = xmlReader.getTextContent(requestPath + "/@id");
				String name = xmlReader.getTextContent(requestPath + "/@name");
				String uri = xmlReader.getTextContent(requestPath + "/uri");
				String method = xmlReader.getTextContent(requestPath + "/method");
				String timeout = xmlReader.getTextContent(requestPath + "/timeout");
				String contentType = xmlReader.getTextContent(requestPath + "/contentType");
				String payload = xmlReader.getTextContent(requestPath + "/payload"); 
				String test = xmlReader.getTextContent(requestPath + "/test");
				HttpRequest request = new HttpRequest(); 
				request.setId(id); 
				request.setName(name); 
				request.setUri(uri); 
				request.setMethod(method); 
				if(timeout != null) { 
					try { 
						request.setTimeout(Integer.parseInt(timeout)); 
					}catch(Exception e){ 
						logger.warn(e.getMessage()); 
					} 
				} 
				request.setContentType(contentType); 
				request.setPayload(payload); 
				request.setTest(test); 
				
				// add element 
				requestList.add(request); 
				logger.debug(request); 
			} 
		}catch(Exception e){ 
			logger.error(e.getMessage(), e); 
			throw e; 
		}finally{ 
			try { is.close(); }catch(Exception e){ logger.warn(e.getMessage()); } 
		} 
	} 
	
	/** 
	 * Returns context object. 
	 * @param id 
	 * @return 
	 * @throws Exception 
	 */ 
	public HttpRequest getHttpRequest(String id) throws Exception { 
		for(HttpRequest request : requestList) { 
			if(request.getId().equals(id)) { 
				return request; 
			} 
		} 
		throw new HttpException(400,"invalid openapi id[" + id + "]"); 
	} 
	
	/**
	 * Return context list. 
	 * @return 
	 * @throws Exception 
	 */ 
	public List<HttpRequest> getHttpRequestList() throws Exception { 
		return requestList; 
	} 
}
