package net.oopscraft.application.admin.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import net.oopscraft.application.core.JsonUtils;
import net.oopscraft.application.core.PageInfo;
import net.oopscraft.application.security.Acl;
import net.oopscraft.application.security.AclService;
import net.oopscraft.application.user.Authority;

@Controller
@RequestMapping("/admin/security")
public class SecurityController {
	
	private static final Logger logger = LoggerFactory.getLogger(SecurityController.class);
	
	@Autowired
	HttpServletResponse response;
	
	@Autowired
	AclService aclService;
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView index() throws Exception {
		ModelAndView modelAndView = new ModelAndView("admin/security.tiles");
		return modelAndView;
	}
	
	@RequestMapping(value = "getAcls", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String getAuthorities(@RequestParam(value = "key", required = false) String key,
			@RequestParam(value = "value", required = false) String value,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "rows", required = false, defaultValue = "10") Integer rows) throws Exception {
		AclService.SearchCondition searchCondition = aclService.new SearchCondition();
		switch ((key == null ? "" : key)) {
		case "uri":
			searchCondition.setUri(value);
			break;
		case "name":
			searchCondition.setName(value);
			break;
		}
		PageInfo pageInfo = new PageInfo(page.intValue(), rows.intValue(), true);
		List<Acl> acls = aclService.getAcls(searchCondition, pageInfo);
		response.setHeader(HttpHeaders.CONTENT_RANGE, pageInfo.getContentRange());
		return JsonUtils.toJson(acls);
	}
	

}
