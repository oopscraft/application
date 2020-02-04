package net.oopscraft.application.api;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import net.oopscraft.application.code.CodeService;
import net.oopscraft.application.code.entity.Code;
import net.oopscraft.application.core.PageInfo;

@RestController
@RequestMapping("/api/code")
public class CodeController {

	@Autowired
	HttpServletResponse response;
	
	@Autowired
	CodeService codeService;
	
	/**
	 * Returns codes
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<Code> getCodes(@ModelAttribute Code code, @ModelAttribute PageInfo pageInfo) throws Exception {
		pageInfo.setEnableTotalCount(true);
		List<Code> codes = codeService.getCodes(code, pageInfo);
		response.setHeader(HttpHeaders.CONTENT_RANGE, pageInfo.getContentRange());
		return codes;
	}
	
	/**
	 * Returns code
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Code getBoard(@ModelAttribute Code code) throws Exception {
		return codeService.getCode(code);
	}
	
}
