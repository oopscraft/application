/*
 * Copyright since 2002 oopscraft.net
 *
 * Everyone is permitted to copy and distribute verbatim copies of this license document, 
 * but changing it is not allowed.
 * Released under the LGPL-3.0 licence
 * https://opensource.org/licenses/lgpl-3.0.html
 */
package net.oopscraft.application.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/user/sign")
public class SignController {
	
	@Autowired
	HttpServletRequest request;
	
	@Autowired
	HttpServletResponse response;
	
	@RequestMapping(value="", method=RequestMethod.GET) 
	public ModelAndView get() throws Exception {
		ModelAndView modelAndView = new ModelAndView("user/sign.jsp");
		return modelAndView;
	}
	
//	@RequestMapping(value="", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
//	public ResponseEntity<?> post(
//		 @RequestParam("id")String admin
//		,@RequestParam("password")String password
//	) throws Exception {
//		return new ResponseEntity<>(null, HttpStatus.OK);
//	}
//	
//	@RequestMapping(value="", method=RequestMethod.DELETE) 
//	public ModelAndView delete() throws Exception {
//		request.getSession().invalidate();
//		return new ModelAndView("redirect:/console");
//	}
}
