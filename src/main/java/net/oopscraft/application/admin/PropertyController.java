package net.oopscraft.application.admin;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import net.oopscraft.application.core.JsonConverter;
import net.oopscraft.application.core.PageInfo;
import net.oopscraft.application.core.StringUtility;
import net.oopscraft.application.property.PropertyService;
import net.oopscraft.application.property.PropertyService.PropertySearchType;
import net.oopscraft.application.property.entity.Property;


@PreAuthorize("hasAuthority('ADMIN_PROPERTY')")
@Controller
@RequestMapping("/admin/property")
public class PropertyController {

	@Autowired
	PropertyService propertyService;

	@Autowired
	HttpServletResponse response;

	/**
	 * Forwards page
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView index() throws Exception {
		ModelAndView modelAndView = new ModelAndView("admin/property.tiles");
		return modelAndView;
	}

	/**
	 * Gets properties
	 * 
	 * @param searchKey
	 * @param searchValue
	 * @param page
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getProperties", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String getProperties(
		@RequestParam(value = "rows")Integer rows,
		@RequestParam(value = "page") Integer page,
		@RequestParam(value = "searchType", required = false) String searchType,
		@RequestParam(value = "searchValue", required = false) String searchValue
	) throws Exception {
		PageInfo pageInfo = new PageInfo(rows, page, true);
		PropertySearchType propertySearchType= null;
		if(StringUtility.isNotEmpty(searchType)) {
			propertySearchType = PropertySearchType.valueOf(searchType);
		}
		List<Property> properties = propertyService.getProperties(pageInfo, propertySearchType, searchValue);
		response.setHeader(HttpHeaders.CONTENT_RANGE, pageInfo.getContentRange());
		return JsonConverter.toJson(properties);
	}

	/**
	 * Gets property details.
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getProperty", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String getProperty(@RequestParam(value = "id") String id) throws Exception {
		Property property = propertyService.getProperty(id);
		return JsonConverter.toJson(property);
	}

	/**
	 * Saves property.
	 * 
	 * @param payload
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "saveProperty", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public void saveProperty(@RequestBody String payload) throws Exception {
		Property property = JsonConverter.toObject(payload, Property.class);
		propertyService.saveProperty(property);
	}

	/**
	 * Deletes property.
	 * 
	 * @param payload
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "deleteProperty", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public void deleteProperty(@RequestParam(value = "id") String id) throws Exception {
		propertyService.deleteProperty(id);
	}

}