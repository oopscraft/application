/*
 * Copyright since 2002 oopscraft.net
 *
 * Everyone is permitted to copy and distribute verbatim copies of this license document, 
 * but changing it is not allowed.
 * Released under the LGPL-3.0 licence
 * https://opensource.org/licenses/lgpl-3.0.html
 */
package net.oopscraft.application.api.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import net.oopscraft.application.code.Code;

/**
 * @author chomookun@gmail.com
 *
 */
@Controller
@RequestMapping("/api/code")
public class CodeController {
	

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Code>> getAll(
		 @RequestParam(value = "rows", defaultValue = "10") int rows
		,@RequestParam(value = "page", defaultValue = "1") int page
	) {
		List<Code> codeList = new ArrayList<Code>();
		for(int i = 0; i < 10; i ++) {
			Code code = new Code();
			code.setId(Integer.toString(i));
			code.setValue(Integer.toString(i));
			codeList.add(code);
		}
		return new ResponseEntity<List<Code>>(codeList, HttpStatus.OK);
	}
	
}
