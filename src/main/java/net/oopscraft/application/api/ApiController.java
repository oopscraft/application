package net.oopscraft.application.api;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import net.oopscraft.application.core.JsonConverter;
import net.oopscraft.application.core.ValueMap;
import net.oopscraft.application.message.MessageService;
import net.oopscraft.application.util.LocaleUtility;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class ApiController {

	@Autowired
	HttpServletRequest request;
	
	@Autowired
	MessageService messageService;
	
	@Autowired
	private RequestMappingHandlerMapping requestMappingHandlerMapping;

	/**
	 * Returns API informations
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<ValueMap> getApis() throws Exception {
		List<ValueMap> apis = new ArrayList<ValueMap>();
		Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
		for (Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
			RequestMappingInfo requestMappingInfo = entry.getKey();
			HandlerMethod handleMethod = entry.getValue();
			ValueMap api = new ValueMap();
			String uri = requestMappingInfo.getPatternsCondition().getPatterns().toArray()[0].toString();
			if(uri.startsWith("/api/")) {
				api.set("uri", uri);
				api.set("method", requestMappingInfo.getMethodsCondition().getMethods().toArray()[0].toString());
				api.set("className", handleMethod.getMethod().getDeclaringClass().getName());
				api.set("methodName", handleMethod.getMethod().getName());
				List<ValueMap> parameters = new ArrayList<ValueMap>();
				for(Parameter parameter : handleMethod.getMethod().getParameters()) {
					ValueMap parameterMap = new ValueMap();
					parameterMap.set("type", parameter.getType().getName());
					try {
						parameterMap.set("value", Class.forName(parameter.getType().getName()).getConstructor().newInstance());
					}catch(Exception ignore) {
						parameterMap.set("test", "");
					}
					parameters.add(parameterMap);
				}
				api.set("parameters", parameters);
				api.set("payload", new Object());
				apis.add(api);
			}
		}
		
		// returns
		return apis;
	}
	
	
	/**
	 * getLocales
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "locales", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> getLocales() throws Exception {
		return new ResponseEntity<>(JsonConverter.toJson(LocaleUtility.getLocales()), HttpStatus.OK);
	}

	/**
	 * getLanguages
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="languages", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> getLanguages() throws Exception {
		return new ResponseEntity<>(JsonConverter.toJson(LocaleUtility.getLanguages()), HttpStatus.OK);
	}

	
}