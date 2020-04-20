package net.oopscraft.application.admin;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import net.oopscraft.application.core.Pagination;
import net.oopscraft.application.property.PropertyService;
import net.oopscraft.application.property.entity.Property;


@PreAuthorize("hasAuthority('ADMN_PROP')")
@Controller
@RequestMapping("/admin/property")
public class PropertyController {

	@Autowired
	HttpServletResponse response;

	@Autowired
	PropertyService propertyService;

	/**
	 * Forwards page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView index() throws Exception {
		ModelAndView modelAndView = new ModelAndView("admin/property.html");
		return modelAndView;
	}
	
	/**
	 * Returns properties
	 * @param role
	 * @param pagination
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getProperties", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public List<Property> getProperties(@ModelAttribute Property property, @ModelAttribute Pagination pagination) throws Exception {
		pagination.setEnableTotalCount(true);
		List<Property> properties = propertyService.getProperties(property, pagination);
		response.setHeader(HttpHeaders.CONTENT_RANGE, pagination.getContentRange());
		return properties;
	}
	
	/**
	 * Returns property
	 * @param property
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getProperty", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public Property getProperty(@ModelAttribute Property property) throws Exception {
		return propertyService.getProperty(property.getId());
	}

	/**
	 * Saves property
	 * @param property
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "saveProperty", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public Property saveProperty(@RequestBody Property property) throws Exception {
		return propertyService.saveProperty(property);
	}
	
	/**
	 * Deletes property
	 * @param property
	 * @throws Exception
	 */
	@RequestMapping(value = "deleteProperty", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public void deleteProperty(@RequestBody Property property) throws Exception {
		propertyService.deleteProperty(property);
	}

}