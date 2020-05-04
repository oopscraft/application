package net.oopscraft.application;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;

import net.oopscraft.application.core.ValueMap;
import net.oopscraft.application.locale.LocaleService;
import net.oopscraft.application.security.UserDetails;
import net.oopscraft.application.user.User;
import net.oopscraft.application.user.UserService;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

@Controller
@ControllerAdvice
@RequestMapping("/")
public class ApplicationWebController {
	
	@Value("${application.theme}")
	String theme;
	
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
	 * Returns locale list
	 * @return
	 * @throws Exception
	 */
	@ModelAttribute("__locales")
	public List<ValueMap> getLocales(HttpServletRequest request) throws Exception {
		return localeService.getLocales(localeResolver.resolveLocale(request));
	}
	
	@ModelAttribute("__user")
	public User getUser() throws Exception {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authentication = securityContext.getAuthentication();
		if(authentication != null && authentication.getPrincipal() instanceof UserDetails) {
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
	public List<ValueMap> getCountries(HttpServletRequest request) throws Exception {
		return localeService.getCountries(localeResolver.resolveLocale(request));
	}

	/**
	 * Returns languages
	 * @return
	 * @throws Exception
	 */
	@ModelAttribute("__languages")
	public List<ValueMap> getLanguages(HttpServletRequest request) throws Exception {
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
