package net.oopscraft.application.api;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;

import net.oopscraft.application.page.Page;
import net.oopscraft.application.page.PageService;

@CrossOrigin
@RestController
@RequestMapping("/api/pages")
public class PageRestController {

	@Autowired
	PageService pageService;
	
	@Autowired
	LocaleResolver localeResolver;
	
	@RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Page getMenu(@PathVariable("id") String id, HttpServletRequest request) throws Exception {
		Page page = pageService.getPage(id);
		
		// translate menu
		Locale locale = localeResolver.resolveLocale(request);
		if(locale != null) {
			page.setContents(page.getContents(locale.getLanguage()));
		}
		
		// returns page.
		return page;
	}
	
}
