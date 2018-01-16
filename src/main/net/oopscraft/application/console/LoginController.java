/*
 * Copyright since 2002 oopscraft.net
 *
 * Everyone is permitted to copy and distribute verbatim copies of this license document, 
 * but changing it is not allowed.
 * Released under the LGPL-3.0 licence
 * https://opensource.org/licenses/lgpl-3.0.html
 */
package net.oopscraft.application.console;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/console/login")
public class LoginController {
	
	@Autowired
	HttpServletRequest request;
	
	@Autowired
	HttpServletResponse response;
	
	@RequestMapping(value="", method=RequestMethod.GET) 
	public ModelAndView get() throws Exception {
		return new ModelAndView("console/login.jsp");
	}
	
	@RequestMapping(value="", method=RequestMethod.POST, produces="application/json")
	public ResponseEntity<?> post(
		 @RequestParam("admin")String admin
		,@RequestParam("password")String password
	) throws Exception {
		
		ConsoleSecurity consoleSecurity = new ConsoleSecurity();
		
		// checks valid administrator user
		if(!consoleSecurity.isValidAdmin(admin)) {
			return new ResponseEntity<>("Administrator Name is Incorrect.", HttpStatus.UNAUTHORIZED);
		}
		
		// checks valid password
		if(!consoleSecurity.isValidPassword(admin, password)) {
			return new ResponseEntity<>("Administrator Password is Incorrect.", HttpStatus.UNAUTHORIZED); 
		}
		
		// creates session
		request.getSession().setAttribute("admin", admin);
		return new ResponseEntity<>(null, HttpStatus.OK);
	}

}
