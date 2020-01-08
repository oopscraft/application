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
import net.oopscraft.application.user.AuthorityService;
import net.oopscraft.application.user.AuthorityService.AuthoritySearchType;
import net.oopscraft.application.user.entity.Authority;

@PreAuthorize("hasAuthority('ADMIN_AUTHORITY')")
@Controller
@RequestMapping("/admin/authority")
public class AuthorityController {

	@Autowired
	AuthorityService authorityService;

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
		ModelAndView modelAndView = new ModelAndView("admin/authority.tiles");
		return modelAndView;
	}

	/**
	 * Gets authorities
	 * 
	 * @param searchKey
	 * @param searchValue
	 * @param page
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getAuthorities", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String getAuthorities(
		@RequestParam(value = "rows")Integer rows,
		@RequestParam(value = "page") Integer page,
		@RequestParam(value = "searchType", required = false) String searchType,
		@RequestParam(value = "searchValue", required = false) String searchValue
	) throws Exception {
		PageInfo pageInfo = new PageInfo(rows, page, true);
		AuthoritySearchType authoritySearchType= null;
		if(StringUtility.isNotEmpty(searchType)) {
			authoritySearchType = AuthoritySearchType.valueOf(searchType);
		}
		List<Authority> properties = authorityService.getAuthorities(pageInfo, authoritySearchType, searchValue);
		response.setHeader(HttpHeaders.CONTENT_RANGE, pageInfo.getContentRange());
		return JsonConverter.toJson(properties);
	}

	/**
	 * Gets authority details.
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getAuthority", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String getAuthority(@RequestParam(value = "id") String id) throws Exception {
		Authority authority = authorityService.getAuthority(id);
		return JsonConverter.toJson(authority);
	}

	/**
	 * Saves authority.
	 * 
	 * @param payload
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "saveAuthority", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public void saveAuthority(@RequestBody String payload) throws Exception {
		Authority authority = JsonConverter.toObject(payload, Authority.class);
		authorityService.saveAuthority(authority);
	}

	/**
	 * Removes authority.
	 * 
	 * @param payload
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "deleteAuthority", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public void deleteAuthority(@RequestParam(value = "id") String id) throws Exception {
		authorityService.deleteAuthority(id);
	}

}