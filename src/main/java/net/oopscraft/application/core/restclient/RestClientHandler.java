package net.oopscraft.application.core.restclient;

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
	
	public String host;
	public RestRequestFactory restRequestFactory;

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
		String response = this.request(restRequest, params, null, 0);
		
		// convert response string into return type.
		Class<?> returnType = method.getReturnType();
		if(returnType == void.class) {
			return null;
		}else if(returnType == int.class) {
			return Integer.parseInt(response);
		}else if(returnType == long.class) {
			return Long.parseLong(response);
		}else if(returnType == double.class) {
			return Double.parseDouble(response);
		}else if(returnType == float.class) {
			return Float.parseFloat(response);
		}else if(returnType == BigDecimal.class) {
			return new BigDecimal(response);
		}else if(returnType == String.class) {
			return response;
		}else if(returnType == List.class) {
			return JsonConverter.convertJsonToObjectList(response, Object.class); 
		}else{
			return JsonConverter.convertJsonToObject(response, returnType); 
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
	 * Call request 
	 * @param request 
	 * @param params 
	 * @param inputStream 
	 * @param length 
	 * @return 
	 * @throws Exception 
	 */ 
	public String request(RestRequest request, ValueMap params, InputStream inputStream, long length) throws Exception { 
		LOGGER.info(String.format("HttpClient.request[id:%s, params:%s]", request.getId(), params));

		String response = null;
		HttpURLConnection connection = null;
		DataOutputStream dos = null;
		InputStream is = null;
		ByteArrayOutputStream baos = null;
		int responseCode = 0;
		try { 
			LOGGER.debug(request.toString());
			String uri = bindParameter(request.getUri(), params, EncodeType.URL);
			String url = this.host + uri ;
			LOGGER.info(String.format("Request[header:%s,method:%s,url:%s]",request,request.getMethod(),url));
			
			// connecting and setting options. 
			connection = this.getConnection(url);
			connection.setRequestMethod(request.getMethod());
			connection.setConnectTimeout(10*1000);	// connect timeout is 10 seconds. 
			connection.setReadTimeout(request.getTimeout()*1000);
			// setting content-type header 
			if(request.getContentType() != null) { 
				connection.setRequestProperty("Content-Type", request.getContentType());
			} 
			// setting custom header 
			setCustomHeaders(connection, request.getCustomHeaders());
			
			// send request body data 
			if(request.getPayload() != null && !request.getPayload().isEmpty()) { 
				String payload = null;
				// defines binding parameter encoding type according to content type. 
				if(request.getContentType().contains("application/x-www-form-urlencoded") 
				|| request.getContentType().contains("multipart/form-data") 
				){
					payload = bindParameter(request.getPayload(), params, EncodeType.URL);
				}
				else if(request.getContentType().contains("application/json")){
					payload = bindParameter(request.getPayload(), params, EncodeType.JSON);
				}else{
					payload = bindParameter(request.getPayload(), params, EncodeType.NONE);
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
			is=connection.getErrorStream();
			if(is==null){
				is=connection.getInputStream();
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
			response = new String(baos.toByteArray(),CHARSET);
			LOGGER.info(String.format("Response[code:%d,message:%s]",responseCode, response));
			
			// checking response HTTP code 
			if(responseCode >= 400) { 
				throw new RestException(responseCode, response != null ? response : connection.getResponseMessage());
			}
		}catch(RestException e){ 
			LOGGER.error(e.getMessage(), e);
			throw e;
		}catch(Exception e){ 
			LOGGER.error(e.getMessage(), e);
			throw new RestException(responseCode, e.getMessage(), e);
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
		return response;																																																																																					
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
						value = JsonConverter.convertObjectToJson(value);
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