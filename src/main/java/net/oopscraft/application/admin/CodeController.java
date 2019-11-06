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

import net.oopscraft.application.code.CodeService;
import net.oopscraft.application.code.CodeService.CodeSearchType;
import net.oopscraft.application.code.entity.Code;
import net.oopscraft.application.core.JsonUtility;
import net.oopscraft.application.core.PageInfo;
import net.oopscraft.application.core.StringUtility;


@PreAuthorize("hasAuthority('ADMIN_CODE')")
@Controller
@RequestMapping("/admin/code")
public class CodeController {

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
	public String getCodes(
		@RequestParam(value = "rows")Integer rows,
		@RequestParam(value = "page") Integer page,
		@RequestParam(value = "searchType", required = false) String searchType,
		@RequestParam(value = "searchValue", required = false) String searchValue
	) throws Exception {
		PageInfo pageInfo = new PageInfo(rows, page, true);
		CodeSearchType codeSearchType = null;
		if(StringUtility.isNotEmpty(searchType)) {
			codeSearchType = CodeSearchType.valueOf(searchType); 
		}
		List<Code> codes = codeService.getCodes(pageInfo, codeSearchType, searchValue);
		response.setHeader(HttpHeaders.CONTENT_RANGE, pageInfo.getContentRange());
		return JsonUtility.toJson(codes);
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
		return JsonUtility.toJson(code);
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
	public void saveCode(@RequestBody String payload) throws Exception {
		Code code = JsonUtility.toObject(payload, Code.class);
		codeService.saveCode(code);
	}

	/**
	 * Removes code.
	 * 
	 * @param payload
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "deleteCode", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public void deleteCode(@RequestParam(value = "id") String id) throws Exception {
		codeService.deleteCode(id);
	}

}