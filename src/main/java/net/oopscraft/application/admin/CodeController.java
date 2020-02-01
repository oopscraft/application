package net.oopscraft.application.admin;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import net.oopscraft.application.code.CodeService;
import net.oopscraft.application.code.entity.Code;
import net.oopscraft.application.core.PageInfo;


//@PreAuthorize("hasAuthority('ADMIN_CODE')")
@Controller
@RequestMapping("/admin/code")
public class CodeController {

	@Autowired
	HttpServletResponse response;

	@Autowired
	CodeService codeService;

	/**
	 * Forwards view page
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView index() throws Exception {
		ModelAndView modelAndView = new ModelAndView("admin/code.html");
		return modelAndView;
	}
	
	/**
	 * Returns code
	 * @param code
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getCodes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public List<Code> getCodes(@ModelAttribute Code code, @ModelAttribute PageInfo pageInfo) throws Exception {
		pageInfo.setEnableTotalCount(true);
		List<Code> codes = codeService.getCodes(code, pageInfo);
		response.setHeader(HttpHeaders.CONTENT_RANGE, pageInfo.getContentRange());
		return codes;
	}
	
	/**
	 * Returns code
	 * @param code
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getCode", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public Code getCode(@ModelAttribute Code code) throws Exception {
		return codeService.getCode(code.getId());
	}

	/**
	 * Saves code
	 * @param code
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "saveCode", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public Code saveCode(@RequestBody Code code) throws Exception {
		return codeService.saveCode(code);
	}
	
	/**
	 * Deletes code
	 * @param code
	 * @throws Exception
	 */
	@RequestMapping(value = "deleteCode", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional(rollbackFor = Exception.class)
	public void deleteCode(@RequestBody Code code) throws Exception {
		codeService.deleteCode(code);
	}

}