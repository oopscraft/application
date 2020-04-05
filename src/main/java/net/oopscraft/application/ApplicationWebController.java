package net.oopscraft.application;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
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
import net.oopscraft.application.security.UserDetails;
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
	
	/**
	 * Default exception handler
	 * @param request
	 * @param response
	 * @param exception
	 * @throws Exception
	 */
	@ExceptionHandler(Exception.class)
	public void handleException(HttpServletRequest request, HttpServletResponse response, Exception exception) throws Exception {
		LOGGER.error(exception.getMessage());
		if("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))){
			ValueMap responseMap = new ValueMap();
			responseMap.set("exception", exception.getClass().getName());
			responseMap.set("message", exception.getMessage());
			responseMap.set("stackTrace", ExceptionUtils.getRootCauseMessage(exception));
	    	response.getWriter().write(JsonConverter.toJson(responseMap));
	    	response.getWriter().flush();
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

	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView index() throws Exception {
		ModelAndView modelAndView = new ModelAndView("index.html");
		return modelAndView;
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
		String resource = request.getRequestURI().replace("public/", "");
		return String.format("forward:/WEB-INF/theme/%s/%s", environment.getProperty("application.theme"), resource);
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
	



	

	
	
	
	
	
	
	
	
	
	
	
	

	
//    @ModelAttribute("__configuration")
//    public Map<String,String> getConfiguration() throws Exception {
//    	return ApplicationContainer.getApplication().getConfiguration();
//    }
	
//	@ModelAttribute("__user")
//	public User getUser() throws Exception {
//		SecurityContext securityContext = SecurityContextHolder.getContext();
//		Authentication authentication = securityContext.getAuthentication();
//		
//		// Anonymous user
//		if(authentication instanceof AnonymousAuthenticationToken) {
//			return new User();
//		}
//
//		// Login user
//		if(authentication.getPrincipal() instanceof UserDetails) {
//			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//			return userDetails.getUser();
//		}
//		
//		// JUNIT Mock Test user
//		if(authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
//			org.springframework.security.core.userdetails.User springUser = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
//			User user = new User();
//			user.setId(springUser.getUsername());
//			for(GrantedAuthority grantedAuthority : springUser.getAuthorities()) {
//				grantedAuthority.getAuthority();
//				Authority authority = new Authority();
//				authority.setId(grantedAuthority.getAuthority());
//				user.addAuthority(authority);
//			}
//			return user;
//		}
//
//		// return null user
//		return new User();
//	}

}
