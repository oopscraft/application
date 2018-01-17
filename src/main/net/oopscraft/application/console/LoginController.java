/*
 * Copyright since 2002 oopscraft.net
 *
 * Everyone is permitted to copy and distribute verbatim copies of this license document, 
 * but changing it is not allowed.
 * Released under the LGPL-3.0 licence
 * https://opensource.org/licenses/lgpl-3.0.html
 */
package net.oopscraft.application.console;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		return new ModelAndView("console/login.jsp");
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
	
	@RequestMapping(value="", method=RequestMethod.POST, produces="application/json")
	public ResponseEntity<?> post(
		 @RequestParam("admin")String admin
		,@RequestParam("password")String password
		,@RequestParam("captcha")String captcha
	) throws Exception {
		
		// checks valid CAPTCHA answer
		if(!captcha.equals(request.getSession(true).getAttribute("captcha"))){
			return new ResponseEntity<>("CAPTCHA Answer is Incorrect.", HttpStatus.UNAUTHORIZED);
		}
		
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
