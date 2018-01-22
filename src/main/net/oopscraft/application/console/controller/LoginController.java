/*
 * Copyright since 2002 oopscraft.net
 *
 * Everyone is permitted to copy and distribute verbatim copies of this license document, 
 * but changing it is not allowed.
 * Released under the LGPL-3.0 licence
 * https://opensource.org/licenses/lgpl-3.0.html
 */
package net.oopscraft.application.console.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import net.oopscraft.application.console.ConsoleSecurity;
import net.oopscraft.application.core.Captcha;
import net.oopscraft.application.core.Captcha.Type;

@Controller
@RequestMapping("/console/login")
public class LoginController {
	
	private static final Log LOG = LogFactory.getLog(LoginController.class);
	
	@Autowired
	HttpServletRequest request;
	
	@Autowired
	HttpServletResponse response;
	
	@RequestMapping(value="", method=RequestMethod.GET) 
	public ModelAndView get() throws Exception {
		ModelAndView modelAndView = new ModelAndView("console/login.jsp");
		return modelAndView;
	}
	
	@RequestMapping(value="/isCaptchaRequired", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> isCaptchaRequired() throws Exception {
		boolean captchaRequired = false;
		if(getLoginFailCount() >= 3) {
			captchaRequired = true;
		}
		return new ResponseEntity<>(captchaRequired, HttpStatus.OK);
	}
	
	@RequestMapping(value="/getCaptchaImage", method=RequestMethod.GET, produces=MediaType.IMAGE_PNG_VALUE)
	public ResponseEntity<byte[]> getCaptchaImage() throws Exception {
		Captcha captcha = new Captcha(Type.CHAR_NUMERIC, 6);
		
		// setting CAPTCHA answer in session
		request.getSession(true).setAttribute("captcha", captcha.getAnswer());
		
		BufferedImage image = captcha.getImage();
		response.setHeader("Cache-Control", "private,no-cache,no-store");
        ByteArrayOutputStream baos = null;
        byte[] bytes = null;
        try {
        	baos = new ByteArrayOutputStream();
        	ImageIO.write(image, "png", baos);
        	bytes = baos.toByteArray();
        }catch(Exception e) {
        	LOG.error(e.getMessage(), e);
        	throw e;
        }finally {
        	baos.close();	
        }
        return new ResponseEntity<byte[]>(bytes,HttpStatus.OK);
	}
	
	@RequestMapping(value="", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> post(
		 @RequestParam("admin")String admin
		,@RequestParam("password")String password
		,@RequestParam(value="captcha", required=false, defaultValue="")String captcha 
	) throws Exception {
		
		// getting session
		HttpSession session = request.getSession(true);
		
		// checks valid CAPTCHA answer
		if(getLoginFailCount() >= 3) {
			if(!captcha.equals(session.getAttribute("captcha"))){
				return new ResponseEntity<>("CAPTCHA Answer is Incorrect.", HttpStatus.UNAUTHORIZED);
			}
		}
		
		// creates consoleSecurity component
		ConsoleSecurity consoleSecurity = new ConsoleSecurity();
		
		// checks valid administrator user
		if(!consoleSecurity.isValidAdmin(admin)) {
			setLoginFailCount(getLoginFailCount()+1);
			return new ResponseEntity<>("Administrator Name is Incorrect.", HttpStatus.UNAUTHORIZED);
		}
		
		// checks valid password
		if(!consoleSecurity.isValidPassword(admin, password)) {
			setLoginFailCount(getLoginFailCount()+1);
			return new ResponseEntity<>("Administrator Password is Incorrect.", HttpStatus.UNAUTHORIZED); 
		}
		
		// creates session
		session.setAttribute("admin", admin);
		setLoginFailCount(0);
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
	
	/**
	 * getting login fail count
	 * @return
	 * @throws Exception
	 */
	private int getLoginFailCount() throws Exception {
		HttpSession session = request.getSession(true);
		int loginFailCount = 0;
		try {
			loginFailCount = (int) session.getAttribute("loginFailCount");
		}catch(Exception ignore){}
		return loginFailCount;
	}
	
	/**
	 * setting login fail count
	 * @param loginFailCount
	 * @throws Exception
	 */
	private void setLoginFailCount(int loginFailCount) throws Exception {
		HttpSession session = request.getSession(true);
		session.setAttribute("loginFailCount", loginFailCount);
	}

}
