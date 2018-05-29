package net.oopscraft.application.core.restclient;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NodeList;

import net.oopscraft.application.core.XPathReader; 

public class RestRequestFactory { 
	private static final Logger LOGGER = LoggerFactory.getLogger(RestRequestFactory.class); 
	private static Map<String,RestRequestFactory> instanceMap = new LinkedHashMap<String,RestRequestFactory>(); 
	List<RestRequest> requestList = new ArrayList<RestRequest>(); 
	
	/** 
	 * Getting Context Manager (Singleton) 
	 * @return 
	 * @throws Exception 
	 */ 
	public synchronized static RestRequestFactory getInstance(String restRequestXml) throws Exception { 
		synchronized(RestRequestFactory.class) { 
			if(!instanceMap.containsKey(restRequestXml)) { 
				RestRequestFactory instance = new RestRequestFactory(restRequestXml); 
				instanceMap.put(restRequestXml, instance); 
			} 
			return instanceMap.get(restRequestXml); 
		} 
	} 
	
	/** 
	 * Constructor 
	 * @throws Exception 
	 */ 
	private RestRequestFactory(String restRequestXml) throws Exception { 
		LOGGER.info("Creating OpenApiContextManager Singleton Instance."); 
		// loading input stream from XML file in class path.
		XPathReader xmlReader = null;
		InputStream is = null; 
		try { 
			is = this.getClass().getResourceAsStream(restRequestXml);
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
				RestRequest request = new RestRequest(); 
				request.setId(id); 
				request.setName(name); 
				request.setUri(uri); 
				request.setMethod(method); 
				if(timeout != null) { 
					try { 
						request.setTimeout(Integer.parseInt(timeout)); 
					}catch(Exception e){ 
						LOGGER.warn(e.getMessage()); 
					} 
				} 
				request.setContentType(contentType); 
				request.setPayload(payload); 
				request.setTest(test); 
				
				// add element 
				requestList.add(request); 

			} 
		}catch(Exception e){ 
			LOGGER.error(e.getMessage(), e); 
			throw e; 
		}finally{ 
			try { is.close(); }catch(Exception e){ LOGGER.warn(e.getMessage()); } 
		} 
	} 
	
	/** 
	 * Returns context object. 
	 * @param id 
	 * @return 
	 * @throws Exception 
	 */ 
	public RestRequest getRestRequest(String id) throws Exception { 
		for(RestRequest request : requestList) { 
			if(request.getId().equals(id)) { 
				return request; 
			} 
		} 
		throw new RestException(400,"invalid openapi id[" + id + "]"); 
	} 
	
	/**
	 * Return context list. 
	 * @return 
	 * @throws Exception 
	 */ 
	public List<RestRequest> getRestRequestList() throws Exception { 
		return requestList; 
	} 
}
