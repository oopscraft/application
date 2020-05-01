package net.oopscraft.application;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;

import net.oopscraft.application.core.JsonConverter;
import net.oopscraft.application.core.ValueMap;
import net.oopscraft.application.locale.LocaleService;
import net.oopscraft.application.message.MessageException;
import net.oopscraft.application.security.entity.UserDetails;
import net.oopscraft.application.user.UserService;
import net.oopscraft.application.user.entity.User;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

@Controller
@ControllerAdvice
@RequestMapping("/")
public class ApplicationWebController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationWebController.class);

	@Value("${application.theme}")
	String theme;
	
	@Autowired
	HttpServletRequest request;
	
	@Autowired
	HttpServletResponse response;
	
	@Autowired
	Environment environment;
	
	@Autowired
	UserService userService;
	
	@Autowired
	LocaleService localeService;
	
	@Autowired
	LocaleResolver localeResolver;
	
	@Autowired
    MessageSource messageSource;
	
	/**
	 * index
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView index() throws Exception {
		ModelAndView modelAndView = new ModelAndView("index.html");
		return modelAndView;
	}
	
	/**
	 * createDefaultErrorMessage
	 * @param request
	 * @param exception
	 * @return
	 * @throws Exception
	 */
	private ValueMap createDefaultErrorMessage(HttpServletRequest request, Exception exception) throws Exception {
		ValueMap errorMessage = new ValueMap();
		errorMessage.set("uri", request.getRequestURI());
		errorMessage.set("method", request.getMethod());
		errorMessage.set("timestamp", new Date());
		errorMessage.set("exception", exception.getClass().getSimpleName());
		errorMessage.set("message", ExceptionUtils.getRootCauseMessage(exception));
		return errorMessage;
	}
	
	/**
	 * responseErrorMessage
	 * @param response
	 * @param errorMessage
	 * @param status
	 * @throws Exception
	 */
	private void responseErrorMessage(HttpServletResponse response, ValueMap errorMessage, int status) throws Exception {
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(JsonConverter.toJson(errorMessage));
		response.setStatus(status);
	}
	
	/**
	 * Default exception handler
	 * @param request
	 * @param response
	 * @param exception
	 * @throws Exception
	 */
	@ExceptionHandler(Exception.class)
	public void handleException(HttpServletRequest request, HttpServletResponse response, Exception exception) throws Exception {
		ValueMap errorMessage = createDefaultErrorMessage(request, exception);
		errorMessage.setString("message", messageSource.getMessage("application.global.exception.Exception", null, localeResolver.resolveLocale(request)));
		if("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))){
			responseErrorMessage(response, errorMessage, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}else {
			throw exception;
		}
	}
	
	/**
	 * Handling Authorization Error
	 * @param request
	 * @param response
	 * @param exception
	 * @throws Exception
	 */
	@ExceptionHandler(AccessDeniedException.class)
	public void handleAccessDeniedException(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception) throws Exception {
		ValueMap errorMessage = createDefaultErrorMessage(request, exception);
		errorMessage.setString("message", messageSource.getMessage("application.global.exception.AccessDeniedException", null, localeResolver.resolveLocale(request)));
		if("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))){
			responseErrorMessage(response, errorMessage, HttpServletResponse.SC_FORBIDDEN);
		}else {
			throw exception;
		}
	}
	
	/**
	 * MethodArgumentNotValidException
	 * @param request
	 * @param response
	 * @param exception
	 * @throws Exception
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public void handleMethodArgumentNotValidException(HttpServletRequest request, HttpServletResponse response, MethodArgumentNotValidException exception) throws Exception {
		ValueMap errorMessage = createDefaultErrorMessage(request, exception);
		StringBuffer message = new StringBuffer();
		if("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))){
			for(FieldError error : exception.getBindingResult().getFieldErrors()) {
				message.append(String.format("[%s.%s] %s", error.getObjectName(), error.getField(), error.getDefaultMessage()));
				break;
			}
			errorMessage.setString("message", message);
			responseErrorMessage(response, errorMessage, HttpServletResponse.SC_BAD_REQUEST);
		}else {
			throw exception;
		}
	}
	
	/**
	 * Handling Business Message Exception
	 * @param request
	 * @param response
	 * @param exception
	 * @throws Exception
	 */
	@ExceptionHandler(MessageException.class)
	public void handleMessageException(HttpServletRequest request, HttpServletResponse response, MessageException exception) throws Exception {
		ValueMap errorMessage = createDefaultErrorMessage(request, exception);
		// TODO get custom message
		if("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))){
			responseErrorMessage(response, errorMessage, HttpServletResponse.SC_BAD_REQUEST);
		}else {
			throw exception;
		}
	}
	
	/**
	 * Returns locale list
	 * @return
	 * @throws Exception
	 */
	@ModelAttribute("__locales")
	public List<ValueMap> getLocales() throws Exception {
		return localeService.getLocales(localeResolver.resolveLocale(request));
	}
	
	@ModelAttribute("__user")
	public User getUser() throws Exception {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authentication = securityContext.getAuthentication();
		if(authentication.getPrincipal() instanceof UserDetails) {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			return userService.getUser(userDetails.getUsername());
		}else {
			return new User();
		}
	}
	
	/**
	 * Returns countries
	 * @return
	 * @throws Exception
	 */
	@ModelAttribute("__countries")
	public List<ValueMap> getCountries() throws Exception {
		return localeService.getCountries(localeResolver.resolveLocale(request));
	}

	/**
	 * Returns languages
	 * @return
	 * @throws Exception
	 */
	@ModelAttribute("__languages")
	public List<ValueMap> getLanguages() throws Exception {
		return localeService.getLanguages(localeResolver.resolveLocale(request));
	}
	
	/**
	 * Returns README.md contents
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "readme", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
	@ResponseBody
	public String readme() throws Exception {
		File file = null;
		try {
			file = new ClassPathResource("README.md").getFile();
		}catch(Exception e) {
			file = new File("README.md");
		}
		return FileUtils.readFileToString(file, "UTF-8");
	}
	
	/**
	 * Returns Plant UML image from requested code
	 * @param plantumlCode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "plantuml", method = RequestMethod.POST, consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.IMAGE_PNG_VALUE)
	@ResponseBody
	public byte[] plantuml(@RequestBody String plantumlCode) throws Exception {
		byte[] imageBytes;
        SourceStringReader reader = new SourceStringReader(plantumlCode);
        OutputStream os = null;
        ByteArrayOutputStream baos = null;
        try {
        	os = new ByteArrayOutputStream();
	        reader.generateImage(os, new FileFormatOption(FileFormat.PNG, false));
	        baos = (ByteArrayOutputStream) os;  
	        imageBytes = baos.toByteArray();
        }catch(Exception e) {
        	throw e;
        }finally {
        	if(baos != null) try { baos.close(); }catch(Exception ignore) {}
        	if(os != null) try { os.close(); }catch(Exception ignore) {}
        }
		return imageBytes;
	}
	
	@RequestMapping(value = "public/**", method = RequestMethod.GET)
	public String forwardPublic(HttpServletRequest request) throws Exception {
		String resource = request.getRequestURI();
		String resourceForward = String.format("forward:/WEB-INF/theme/%s%s", environment.getProperty("application.theme"), resource);
		return resourceForward;
	}
	
	/**
	 * Login page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "login", method = RequestMethod.GET)
	public ModelAndView login() throws Exception {
		ModelAndView modelAndView = new ModelAndView("login.html");
		return modelAndView;
	}
	
}
