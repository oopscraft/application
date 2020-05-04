package net.oopscraft.application.api;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ValueConstants;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import net.oopscraft.application.core.JsonConverter;
import net.oopscraft.application.core.Pagination;
import net.oopscraft.application.core.ValueMap;
import net.oopscraft.application.locale.LocaleService;
import net.oopscraft.application.message.MessageService;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class ApiRestController {
	
	@Autowired
	LocaleResolver localeResolver;
	
	@Autowired
	LocaleService localeService;
	
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
	@ResponseBody
	public List<ValueMap> getApis(@RequestParam Map<String,String> paramMap, HttpServletResponse response) throws Exception {
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
				List<ValueMap> variables = new ArrayList<ValueMap>();
				List<ValueMap> parameters = new ArrayList<ValueMap>();
				ValueMap payload = new ValueMap();
				for(Parameter parameter : handleMethod.getMethod().getParameters()) {
					for(Annotation annotation : parameter.getAnnotations()) {
						if(annotation instanceof PathVariable) {
							ValueMap variableMap = new ValueMap();
							String name = ((PathVariable) annotation).value();
							variableMap.set("name", name);
							variables.add(variableMap);
						}else if(annotation instanceof RequestParam) {
							ValueMap parameterMap = new ValueMap();
							String name = ((RequestParam) annotation).value();
							String value = ((RequestParam) annotation).defaultValue();
							value = value.contentEquals(ValueConstants.DEFAULT_NONE) ? "" : value;
							parameterMap.set("name", name);
							parameterMap.set("value", value);
							parameters.add(parameterMap);
						}else if(annotation instanceof ModelAttribute) {
							Object parameterObj = parameter.getType().getConstructor().newInstance();
							ValueMap parameterObjMap = JsonConverter.toObject(JsonConverter.toJson(parameterObj), ValueMap.class);
							for(String name : parameterObjMap.keySet()) {
								ValueMap parameterMap = new ValueMap();
								parameterMap.set("name", name);
								parameterMap.set("value", parameterObjMap.get(name));
								parameters.add(parameterMap);
							}
						}else if(annotation instanceof RequestBody) {
							Object parameterObj = parameter.getType().getConstructor().newInstance();
							ValueMap parameterObjMap = JsonConverter.toObject(JsonConverter.toJson(parameterObj), ValueMap.class);
							for(String name : parameterObjMap.keySet()) {
								payload.set(name, parameterObjMap.get(name));
							}
						}
					}
				}
				api.set("variables", variables);
				api.set("parameters", parameters);
				api.set("payload", payload.isEmpty() ? null : JsonConverter.toJson(payload));
				apis.add(api);
			}
		}

		// pagination
		List<ValueMap> apisPage = new ArrayList<ValueMap>();
		Pagination pagination = new Pagination();
		pagination.setEnableTotalCount(true);
		pagination.setPage(Integer.parseInt(paramMap.getOrDefault("page", "1")));
		pagination.setRows(Integer.parseInt(paramMap.getOrDefault("rows", "10")));
		int count = 0;
		for(ValueMap api : apis) {

			// search by URI
			if(paramMap.containsKey("uri")) {
				if(!api.getString("uri").contains((String)paramMap.get("uri"))){
					continue;
				}
			}
			// search by method
			if(paramMap.containsKey("method")) {
				if(!api.getString("method").contains((String)paramMap.get("method"))){
					continue;
				}
			}
			// search by className
			if(paramMap.containsKey("className")) {
				if(!api.getString("className").contains((String)paramMap.get("className"))){
					continue;
				}
			}
			// search by className
			if(paramMap.containsKey("methodName")) {
				if(!api.getString("methodName").contains((String)paramMap.get("methodName"))){
					continue;
				}
			}
			count ++;
			if(pagination.getOffset() < count && count <= pagination.getOffset() + pagination.getLimit()) {
				apisPage.add(api);	
			}
		}

		// returns
		pagination.setTotalCount(count);
		response.setHeader(HttpHeaders.CONTENT_RANGE, pagination.getContentRange());		
		return apisPage;
	}
	
	/**
	 * Returns locale list
	 * @return
	 * @throws Exception
	 */
	@Description("Getting list of locale.")
	@RequestMapping(value = "locale", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public List<ValueMap> getLocales(HttpServletRequest request) throws Exception {
		return localeService.getLocales(localeResolver.resolveLocale(request));
	}
	
	/**
	 * Returns countries
	 * @return
	 * @throws Exception
	 */
	@Description("Getting list of country.")
	@RequestMapping(value = "country", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public List<ValueMap> getCountries(HttpServletRequest request) throws Exception {
		return localeService.getCountries(localeResolver.resolveLocale(request));
	}

	/**
	 * Returns languages
	 * @return
	 * @throws Exception
	 */
	@Description("Getting list of language.")
	@RequestMapping(value = "language", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public List<ValueMap> getLanguages(HttpServletRequest request) throws Exception {
		return localeService.getLanguages(localeResolver.resolveLocale(request));
	}

	
}