package net.oopscraft.application.admin.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import net.oopscraft.application.core.JsonUtils;
import net.oopscraft.application.core.PageInfo;
import net.oopscraft.application.core.TextTable;
import net.oopscraft.application.code.Code;
import net.oopscraft.application.code.CodeService;


@PreAuthorize("hasAuthority('ADMIN_CODE')")
@Controller
@RequestMapping("/admin/code")
public class CodeController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@Autowired
	CodeService codeService;

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
		ModelAndView modelAndView = new ModelAndView("admin/code.tiles");
		return modelAndView;
	}

	/**
	 * Gets codes
	 * 
	 * @param searchKey
	 * @param searchValue
	 * @param page
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getCodes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional
	public String getCodes(@RequestParam(value = "key", required = false) String key,
			@RequestParam(value = "value", required = false) String value,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "rows", required = false, defaultValue = "10") Integer rows) throws Exception {
		CodeService.CodeSearch searchCondition = codeService.new CodeSearch();
		switch ((key == null ? "" : key)) {
		case "id":
			searchCondition.setId(value);
			break;
		case "name":
			searchCondition.setName(value);
			break;
		}
		PageInfo pageInfo = new PageInfo(page.intValue(), rows.intValue(), true);
		List<Code> roles = codeService.getCodes(searchCondition, pageInfo);
		LOGGER.debug("{}", new TextTable(roles));
		response.setHeader(HttpHeaders.CONTENT_RANGE, pageInfo.getContentRange());
		return JsonUtils.toJson(roles);
	}

	/**
	 * Gets code details.
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getCode", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional
	public String getCode(@RequestParam(value = "id") String id) throws Exception {
		Code code = codeService.getCode(id);
		return JsonUtils.toJson(code);
	}

	/**
	 * Saves code.
	 * 
	 * @param payload
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "saveCode", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public String saveCode(@RequestBody String payload) throws Exception {
		Code code = JsonUtils.toObject(payload, Code.class);
		code = codeService.saveCode(code);
		return JsonUtils.toJson(code);
	}

	/**
	 * Removes code.
	 * 
	 * @param payload
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "removeCode", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public String removeCode(@RequestParam(value = "id") String id) throws Exception {
		Code code = codeService.removeCode(id);
		return JsonUtils.toJson(code);
	}

}