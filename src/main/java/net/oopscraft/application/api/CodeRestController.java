package net.oopscraft.application.api;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import net.oopscraft.application.code.Code;
import net.oopscraft.application.code.CodeService;
import net.oopscraft.application.core.Pagination;

@CrossOrigin
@RestController
@RequestMapping("/api/codes")
public class CodeRestController {

	@Autowired
	CodeService codeService;
	
	/**
	 * Returns codes
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<Code> getCodes(@ModelAttribute Code code, @ModelAttribute Pagination pagination, HttpServletResponse response) throws Exception {
		pagination.setEnableTotalCount(true);
		List<Code> codes = codeService.getCodes(code, pagination);
		response.setHeader(HttpHeaders.CONTENT_RANGE, pagination.getContentRange());
		return codes;
	}
	
	/**
	 * Returns code
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public Code getBoard(@PathVariable("id") String id) throws Exception {
		Code code = new Code();
		code.setId(id);
		return codeService.getCode(code);
	}
	
}
