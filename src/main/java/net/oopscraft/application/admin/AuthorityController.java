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

import net.oopscraft.application.core.PageInfo;
import net.oopscraft.application.user.AuthorityService;
import net.oopscraft.application.user.entity.Authority;

//@PreAuthorize("hasAuthority('ADMIN_AUTHORITY')")
@Controller
@RequestMapping("/admin/authority")
public class AuthorityController {

	@Autowired
	HttpServletResponse response;
	
	@Autowired
	AuthorityService authorityService;
	
	/**
	 * Forwards view page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView index() throws Exception {
		ModelAndView modelAndView = new ModelAndView("admin/authority.html");
		return modelAndView;
	}
	
	/**
	 * Returns authorities
	 * @param authority
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getAuthorities", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public List<Authority> getAuthorities(@ModelAttribute Authority authority, @ModelAttribute PageInfo pageInfo) throws Exception {
		pageInfo.setEnableTotalCount(true);
		List<Authority> authorities = authorityService.getAuthorities(authority, pageInfo);
		response.setHeader(HttpHeaders.CONTENT_RANGE, pageInfo.getContentRange());
		return authorities;
	}
	
	/**
	 * Return authority
	 * @param authority
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getAuthority", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public Authority getAuthority(@ModelAttribute Authority authority) throws Exception {
		return authorityService.getAuthority(authority.getId());
	}

	/**
	 * Saves authority
	 * @param authority
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "saveAuthority", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public Authority saveAuthority(@RequestBody Authority authority) throws Exception {
		return authorityService.saveAuthority(authority);
	}
	
	/**
	 * Deletes authority
	 * @param authority
	 * @throws Exception
	 */
	@RequestMapping(value = "deleteAuthority", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public void deleteAuthority(@RequestBody Authority authority) throws Exception {
		authorityService.deleteAuthority(authority);
	}

}