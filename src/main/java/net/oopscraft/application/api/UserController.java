package net.oopscraft.application.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.oopscraft.application.core.JsonUtility;
import net.oopscraft.application.user.User;
import net.oopscraft.application.user.UserService;
import net.oopscraft.application.user.security.UserDetails;

@Controller
@RequestMapping("/api/user")
public class UserController {
	
	@Autowired
	private UserService userService;

	/**
	 * Adds new user
	 * @param payload
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@Transactional(rollbackFor = Exception.class)
	public ResponseEntity<?> addUser(@RequestBody String payload) throws Exception {
		User payloadUser = JsonUtility.toObject(payload, User.class);

		// exiting user
		User user = userService.getUser(payloadUser.getId());
		if(user != null) {
			return new ResponseEntity<>(String.format("ID[%s] is Alreay Exists.",user.getId()), HttpStatus.NOT_ACCEPTABLE);
		}
		
		// saves user
		userService.saveUser(payloadUser);
		
		// response
		return new ResponseEntity<>(JsonUtility.toJson(payloadUser), HttpStatus.OK);
	}
	
	/**
	 * Updates user
	 * @param id
	 * @param payload
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> updateUser(@PathVariable("id")String id, @RequestBody String payload) throws Exception {
		
		User payloadUser = JsonUtility.toObject(payload, User.class);
		payloadUser.setId(id);
		
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authentication = securityContext.getAuthentication();
		
		// checks login user
		if(authentication instanceof AnonymousAuthenticationToken) {
			return new ResponseEntity<>("Not Login User", HttpStatus.NOT_ACCEPTABLE);
		}
		
		// check self profile
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		if(id.equals(userDetails.getUsername()) == false) {
			return new ResponseEntity<>("Invalid User ID", HttpStatus.NOT_ACCEPTABLE);
		}
		
		// saves user
		userService.saveUser(payloadUser);
		
		// response
		return new ResponseEntity<>(JsonUtility.toJson(payloadUser), HttpStatus.OK);
	}

}
