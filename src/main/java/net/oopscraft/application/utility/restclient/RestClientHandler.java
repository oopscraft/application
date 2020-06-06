package net.oopscraft.application.utility.restclient;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.X509Certificate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.oopscraft.application.core.JsonConverter;
import net.oopscraft.application.core.ValueMap;

public class RestClientHandler implements InvocationHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RestClientHandler.class);
	private static final String CHARSET = "UTF-8";
	enum EncodeType {NONE, URL, JSON}
	
	private String host;
	private RestRequestFactory restRequestFactory;
	private RestListener restListener;

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		String methodName = method.getName();
		RestRequest restRequest = restRequestFactory.getRestRequest(methodName);
		
		// setting parameter from interface annotations.
		ValueMap params = new ValueMap();
		Annotation[][] annotationsArray = method.getParameterAnnotations();
		for(int i = 0; i < annotationsArray.length; i ++) {
			Annotation annotation = annotationsArray[i][0];
			RestParam restParam = (RestParam) annotation;
			String name = restParam.value();
			Object value = args[i];
			params.set(name, value);
        }
		
		// request
		RestResponse restResponse = this.request(restRequest, params, null, 0);
		
		// convert response string into return type.
		Class<?> returnType = method.getReturnType();
		if(returnType == void.class) {
			return null;
		}else if(returnType == int.class) {
			return Integer.parseInt(restResponse.getResponseMessage());
		}else if(returnType == long.class) {
			return Long.parseLong(restResponse.getResponseMessage());
		}else if(returnType == double.class) {
			return Double.parseDouble(restResponse.getResponseMessage());
		}else if(returnType == float.class) {
			return Float.parseFloat(restResponse.getResponseMessage());
		}else if(returnType == BigDecimal.class) {
			return new BigDecimal(restResponse.getResponseMessage());
		}else if(returnType == String.class) {
			return restResponse.getResponseMessage();
		}else if(returnType == List.class) {
			return JsonConverter.toList(restResponse.getResponseMessage(), Object.class); 
		}else{
			return JsonConverter.toObject(restResponse.getResponseMessage(), returnType); 
		}
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the restRequestFactory
	 */
	public RestRequestFactory getRestRequestFactory() {
		return restRequestFactory;
	}

	/**
	 * @param restRequestFactory the restRequestFactory to set
	 */
	public void setRestRequestFactory(RestRequestFactory restRequestFactory) {
		this.restRequestFactory = restRequestFactory;
	}
	
	/**
	 * setRestListener
	 * @param restListener
	 */
	public void setRestListener(RestListener restListener) {
		this.restListener = restListener;
	}

	/** 
	 * Call request 
	 * @param restRequest 
	 * @param params 
	 * @param inputStream 
	 * @param length 
	 * @return 
	 * @throws Exception 
	 */ 
	public RestResponse request(RestRequest restRequest, ValueMap params, InputStream inputStream, long length) throws Exception { 
		LOGGER.info(String.format("HttpClient.request[id:%s, params:%s]", restRequest.getId(), params));

		// restListener.before
		if(restListener != null) {
			restListener.before(restRequest);
		}
		
		RestResponse restResponse = new RestResponse();
		HttpURLConnection connection = null;
		DataOutputStream dos = null;
		InputStream is = null;
		ByteArrayOutputStream baos = null;
		try { 
			LOGGER.debug(restRequest.toString());
			String uri = bindParameter(restRequest.getUri(), params, EncodeType.URL);
			String url = this.host + uri ;
			LOGGER.info(String.format("Request[header:%s,method:%s,url:%s]",restRequest,restRequest.getMethod(),url));
			
			// connecting and setting options. 
			connection = this.getConnection(url);
			connection.setRequestMethod(restRequest.getMethod());
			connection.setConnectTimeout(10*1000);	// connect timeout is 10 seconds. 
			connection.setReadTimeout(restRequest.getTimeout()*1000);
			// setting content-type header 
			if(restRequest.getContentType() != null) { 
				connection.setRequestProperty("Content-Type", restRequest.getContentType());
			} 
			// setting custom header 
			setCustomHeaders(connection, restRequest.getCustomHeaders());
			
			// send request body data 
			if(restRequest.getPayload() != null && !restRequest.getPayload().isEmpty()) { 
				String payload = null;
				// defines binding parameter encoding type according to content type. 
				if(restRequest.getContentType().contains("application/x-www-form-urlencoded") 
				|| restRequest.getContentType().contains("multipart/form-data") 
				){
					payload = bindParameter(restRequest.getPayload(), params, EncodeType.URL);
				}
				else if(restRequest.getContentType().contains("application/json")){
					payload = bindParameter(restRequest.getPayload(), params, EncodeType.JSON);
				}else{
					payload = bindParameter(restRequest.getPayload(), params, EncodeType.NONE);
				}
				LOGGER.info(String.format("Payload[%s]",payload));
				connection.setDoOutput(true);
				dos=new DataOutputStream(connection.getOutputStream());
				dos.write(payload.getBytes(CHARSET));
				dos.flush();
			} 
			
			// send
			if(inputStream != null) {
				connection.setRequestProperty("Cache-Control","no-cache");
				connection.setRequestProperty("Content-Length",Long.toString(length));
				connection.setDoOutput(true);
				dos=new DataOutputStream(connection.getOutputStream());
				byte[] buffer = new byte[1024];
				long totalLen = length;
				long count = 0;
				long currentLen = 0;
				int len = 0;
				LOGGER.info(String.format("send binary[%d/%d]",currentLen,totalLen));
				while((len=inputStream.read(buffer))!=-1){ 
					count ++;
					currentLen += len;
					if(count%1024 == 0) { 
						LOGGER.info(String.format("send binary[%d/%d]", currentLen, totalLen));
					} 
					dos.write(buffer,0,len);
					dos.flush();
				}
				LOGGER.info(String.format("send binary[%d/%d]",currentLen,totalLen));
				dos.flush();
				dos.close();
			}
		
			// reading
			try {
				is=connection.getInputStream();
			}catch(Exception e) {
				LOGGER.error(e.getMessage());
			}
			if(is == null) {
				is=connection.getErrorStream();
			}
			
			// reading from stream 
			baos = new ByteArrayOutputStream();
			int len = 0;
			byte[] buffer = new byte[1024];
			while((len=is.read(buffer))!=-1){
				baos.write(buffer, 0, len);
				baos.flush();
			} 
			
			// defines response string. 
			int responseCode = connection.getResponseCode();
			String responseMessage = new String(baos.toByteArray(),CHARSET);
			LOGGER.info(String.format("Response[code:%d,message:%s]",responseCode, responseMessage));
			restResponse.setResponseCode(responseCode);
			restResponse.setResponseMessage(responseMessage);
			
			// setting response header
			Map<String,String> headerMap = new LinkedHashMap<String,String>();
			for(Map.Entry<String,List<String>> entry: connection.getHeaderFields().entrySet()){
				String name = entry.getKey();
				String value = connection.getHeaderField(name);
				headerMap.put(name, value);
			}
			restResponse.setHeaderMap(headerMap);
			
			// restListener.after
			if(restListener != null) {
				restListener.after(restResponse);
			}
			
			// checking response HTTP code 
			if(restResponse.getResponseCode() >= 400) { 
				throw new RestException(responseCode, responseMessage != null ? responseMessage : connection.getResponseMessage());
			}
		}catch(RestException e){ 
			LOGGER.error(e.getMessage(), e);
			throw e;
		}catch(Exception e){ 
			LOGGER.error(e.getMessage(), e);
			throw new RestException(restResponse.getResponseCode(), e.getMessage(), e);
		}finally{ 
			if(is != null) { 
				try { is.close(); }catch(Exception e){ LOGGER.warn(e.getMessage()); } 
			}
			if(dos != null) { 
				try {	dos.close(); }catch(Exception e){ LOGGER.warn(e.getMessage()); } 
			} 
			if(baos != null) { 
				try { baos.close(); }catch(Exception e){ LOGGER.warn(e.getMessage()); } 
			} 
			if(connection != null) { 
				try { connection.disconnect(); }catch(Exception e){ LOGGER.warn(e.getMessage()); } 
			} 
		} 
		
		// return
		return restResponse;																																																																																					
	}

	/** 
	 * Getting HTTP(s) Connection 
	 * @param url 
	 * @return 
	 * @throws Exception 
	 */ 
	private HttpURLConnection getConnection(String url) throws Exception { 
		HttpURLConnection connection = null;
		if(url.startsWith("https")) { 
			// Create a trust manager that does not validate certificate chains 
			TrustManager[] trustAllCerts = new TrustManager[] {
				new X509TrustManager() { 
					public X509Certificate[] getAcceptedIssuers() { 
						// according to PMD 
						return new X509Certificate[]{};
					}
					public void checkClientTrusted(X509Certificate[] certs, String authType) { 
						// relevant to private certificates access. 
					} 
					public void checkServerTrusted(X509Certificate[] certs, String authType) { 
						// relevant to private certificates access. 
					} 
				} 
			};
			// Install the all-trusting trust manager 
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			// Create all-trusting host name verifier 
			HostnameVerifier allHostsValid = new HostnameVerifier() { 
				public boolean verify(String hostname, SSLSession session) { return true;
				}

			};
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
			connection=(HttpsURLConnection)new URL(url).openConnection();
			
		}else{
			connection=(HttpURLConnection)new URL(url).openConnection();}return connection;
		}

	/** 
	 * Setting HTTP header 
	 * @param connection 
	 * @param headers 
	 * @throws Exception 
	 */ 
	private void setCustomHeaders(HttpURLConnection connection, Map<String,String> headers) throws Exception { 
		for(String headerName : headers.keySet()) { 
			Object headerValue = headers.get(headerName);
			connection.setRequestProperty(headerName, (String)headerValue);
		} 
	}

	/** 
	 * bind parameter value. 
	 * @param string 
	 * @param param 
	 * @return 
	 * @throws Exception 
	 */ 
	protected String bindParameter(String string, ValueMap param, EncodeType encodeType) throws Exception { 
		if(string == null || string.trim().length() < 1) { 
			return string;
		} 
		Pattern p = Pattern.compile("(?:\\#\\{)([^\\#\\{\\}]*)(?:\\})");
		Matcher m = p.matcher(string);
		StringBuffer sb = new StringBuffer();
		while(m.find()) { 
			String name = m.group(1);
			// Encoding value. 
			Object value = param.get(name);
			switch(encodeType){ 
				case URL : 
					value = URLEncoder.encode(value == null ? "" : value.toString(), CHARSET);
				break;
				case JSON : 
					if(value == null) { 
						value = "null";
					}else if(value instanceof String) { 
						value = "\"" + JsonConverter.stringify(value.toString()) + "\"";
					}else if( value instanceof BigDecimal) { 
						value = value.toString();
					}else if(value instanceof Object){ 
						value = JsonConverter.toJson(value);
					} 
				break;
				case NONE: 
					value = value == null ? "" : value;
				break;
				default : 
				break;
			} 
			LOGGER.debug("+ " + name + ":" + value);
			m.appendReplacement(sb, Matcher.quoteReplacement(value.toString()));
		} 
		m.appendTail(sb);
		return sb.toString();
	}
	
}