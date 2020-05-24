package net.oopscraft.application;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import net.oopscraft.application.core.ValueMap;
import net.oopscraft.application.locale.LocaleService;
import net.oopscraft.application.property.Property;
import net.oopscraft.application.property.PropertyService;
import net.oopscraft.application.security.UserDetails;
import net.oopscraft.application.user.User;
import net.oopscraft.application.user.UserService;

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
	PropertyService propertyService;
	
	@Autowired
	LocaleService localeService;
	
	@Autowired
	LocaleResolver localeResolver;
	
	@Autowired
    MessageSource messageSource;
	
	@ModelAttribute("_theme")
	public String getTheme() throws Exception {
		return propertyService.getProperty("APP_THEM").getValue().trim();
	}
	
	@ModelAttribute("_device")
	public String getDevice(HttpServletRequest request) throws Exception {
		Device device = DeviceUtils.getCurrentDevice(request);
		if(device.isMobile() || device.isTablet()) {
			return "mobile";
		}else {
			return "normal";
		}
	}
	
	/**
	 * Returns locale list
	 * @return
	 * @throws Exception
	 */
	@ModelAttribute("_locales")
	public List<ValueMap> getLocales(HttpServletRequest request) throws Exception {
		return localeService.getLocales(localeResolver.resolveLocale(request));
	}
	
	/**
	 * Returns countries
	 * @return
	 * @throws Exception
	 */
	@ModelAttribute("_countries")
	public List<ValueMap> getCountries(HttpServletRequest request) throws Exception {
		return localeService.getCountries(localeResolver.resolveLocale(request));
	}

	/**
	 * Returns languages
	 * @return
	 * @throws Exception
	 */
	@ModelAttribute("_languages")
	public List<ValueMap> getLanguages(HttpServletRequest request) throws Exception {
		return localeService.getLanguages(localeResolver.resolveLocale(request));
	}
	
	@ModelAttribute("_user")
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
	 * index
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView index() throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		
		// in case of index page id is setting.
		Property property = propertyService.getProperty("APP_INDX_URI");
		if(property != null && StringUtils.isNotBlank(property.getValue())) {
			String indexUri = property.getValue();
		    RedirectView redirectView = new RedirectView(indexUri);
		    redirectView.setExposeModelAttributes(false);
			modelAndView.setView(redirectView);
		}
		// index page id setting not found
		else{
			modelAndView.setViewName("index.html");
		}
		
		// return view
		return modelAndView;
	}
	
	/**
	 * public
	 */
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
