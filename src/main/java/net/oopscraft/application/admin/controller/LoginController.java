package net.oopscraft.application.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin/login")
public class LoginController {

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView login() throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("admin/login.jsp");
		return modelAndView;
	}
	
	
	
	
//	@RequestMapping(value="", method=RequestMethod.GET) 
//	public ModelAndView get() throws Exception {
//		ModelAndView modelAndView = new ModelAndView("admin/sign.jsp");
//		return modelAndView;
//	}
//	
//	@RequestMapping(value="/isCaptchaRequired", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
//	public ResponseEntity<?> isCaptchaRequired() throws Exception {
//		boolean captchaRequired = false;
//		if(getLoginFailCount() >= 2) {
//			captchaRequired = true;
//		}
//		return new ResponseEntity<>(captchaRequired, HttpStatus.OK);
//	}
//	
//	@RequestMapping(value="/prepareCaptcha", method=RequestMethod.GET)
//	public ResponseEntity<?> prepareCaptcha() throws Exception {
//		String answer = AnswerGenerator.generateAlphabetNumeric(6);
//		CaptchaUtility.prepareCaptcha(answer, request, response);
//		return new ResponseEntity<String>(HttpStatus.OK);
//	}
//	
//	@RequestMapping(value="/getCaptchaImage", method=RequestMethod.GET)
//	public ResponseEntity<?> getCaptchaImage() throws Exception {
//		CaptchaUtility.writeCaptchaImage(request, response);
//		return new ResponseEntity<String>(HttpStatus.OK);
//	}
//	
//	@RequestMapping(value="/getCaptchaAudio", method=RequestMethod.GET)
//	public ResponseEntity<?> getCaptchaAudio() throws Exception {
//		CaptchaUtility.writeCaptchaAudio(request, response);
//		return new ResponseEntity<String>(HttpStatus.OK);
//	}
//	
//	@RequestMapping(value="", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
//	public ResponseEntity<?> post(
//		 @RequestParam("admin")String admin
//		,@RequestParam("password")String password
//		,@RequestParam(value="captcha", required=false, defaultValue="")String captcha 
//	) throws Exception {
//		
//		// getting session
//		HttpSession session = request.getSession(true);
//		
//		// checks valid CAPTCHA answer
//		if(getLoginFailCount() >= 2) {
//			try {
//				CaptchaUtility.checkCaptcha(captcha, request, response);
//			}catch(CaptchaException e) {
//				return new ResponseEntity<>("CAPTCHA Answer is Incorrect.", HttpStatus.UNAUTHORIZED);
//			}
//		}
//		
////		// creates consoleSecurity component
////		AdminSecurity consoleSecurity = new AdminSecurity();
////		
////		// checks valid administrator user
////		if(!consoleSecurity.isValidAdmin(admin)) {
////			setLoginFailCount(getLoginFailCount()+1);
////			return new ResponseEntity<>("Administrator Name is Incorrect.", HttpStatus.UNAUTHORIZED);
////		}
////		
////		// checks valid password
////		if(!consoleSecurity.isValidPassword(admin, password)) {
////			setLoginFailCount(getLoginFailCount()+1);
////			return new ResponseEntity<>("Administrator Password is Incorrect.", HttpStatus.UNAUTHORIZED); 
////		}
//		
//		// creates session
//		session.setAttribute("admin", admin);
//		setLoginFailCount(0);
//		return new ResponseEntity<>(null, HttpStatus.OK);
//	}
//	
//	/**
//	 * getting login fail count
//	 * @return
//	 * @throws Exception
//	 */
//	private int getLoginFailCount() throws Exception {
//		HttpSession session = request.getSession(true);
//		int loginFailCount = 0;
//		try {
//			loginFailCount = (int) session.getAttribute("loginFailCount");
//		}catch(Exception ignore){}
//		return loginFailCount;
//	}
//	
//	/**
//	 * setting login fail count
//	 * @param loginFailCount
//	 * @throws Exception
//	 */
//	private void setLoginFailCount(int loginFailCount) throws Exception {
//		HttpSession session = request.getSession(true);
//		session.setAttribute("loginFailCount", loginFailCount);
//	}
//
//	@RequestMapping(value="", method=RequestMethod.DELETE) 
//	public ModelAndView delete() throws Exception {
//		request.getSession().invalidate();
//		return new ModelAndView("redirect:/console");
//	}

}
