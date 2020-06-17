package net.oopscraft.application.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import net.oopscraft.application.ApplicationConfig;
import net.oopscraft.application.message.MessageException;
import net.oopscraft.application.security.AuthenticationHandler;
import net.oopscraft.application.security.AuthenticationProvider;
import net.oopscraft.application.security.SecurityPolicy;
import net.oopscraft.application.user.Verification.IssueType;

@Controller
@RequestMapping("/user/join")
public class JoinController {
	
	@Autowired
	ApplicationConfig applicationConfig;
	
	@Autowired
	VerificationService verificationService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	AuthenticationProvider authenticationProvider;
	
	@Autowired
	AuthenticationHandler authenticationHandler;
	
	/**
	 * join
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView index() throws Exception {
		ModelAndView modelAndView = new ModelAndView("user/join.html");
		return modelAndView;
	}
	
	/**
	 * Issue verification
	 * @param request
	 * @param email
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "issueVerification", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	@Transactional
	public Verification issueVerification(
		  HttpServletRequest request
		 ,@RequestParam("email")String email
	) throws Exception {
		
		// checks already used email address.
		User user = userService.getUserByEmail(email);
		if(user != null) {
			throw new MessageException("application.global.alreadyRegisteredItem", new String[]{"application.user.email"}, request);
		}
		
		// issues verification object
		Verification verification = verificationService.issueVerification(IssueType.EMAIL, email, null);
		return verification;
	}
	
	/**
	 * join
	 * @param request
	 * @param response
	 * @param email
	 * @param verificationId
	 * @param verificationCode
	 * @param name
	 * @param password
	 * @param passwordConfirm
	 * @throws Exception
	 */
	@RequestMapping(value = "process", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@Transactional
	public void process(
		 HttpServletRequest request
		,HttpServletResponse response
		,@RequestParam("email") String email
		,@RequestParam("verificationId") String verificationId
		,@RequestParam("verificationCode") String verificationCode
		,@RequestParam("name") String name
		,@RequestParam("password") String password
		,@RequestParam("passwordConfirm") String passwordConfirm
	) throws Exception {
		
		// checks verification info
		boolean isCorrect = verificationService.isCorrectCode(verificationId, verificationCode);
		if(!isCorrect) {
			throw new MessageException("application.global.itemNotMatch", new String[]{"application.verification.code"}, request);
		}
		
		// creates new user
		User user = new User();
		user.setEmail(email);
		user.setName(name);
		user.setPassword(password);
		
		// setting default status
		if(applicationConfig.getSecurityPolicy() == SecurityPolicy.ANONYMOUS) {
			user.setStatus(User.Status.ACTIVE);
		}else {
			user.setStatus(User.Status.SUSPENDED);
		}
		
		// saves user
		userService.saveUser(user);

		// login process
		Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
		authentication = authenticationProvider.authenticate(authentication);
		authenticationHandler.onAuthenticationSuccess(request, response, authentication);
	}

}
