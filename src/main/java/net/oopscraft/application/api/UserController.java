package net.oopscraft.application.api;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import net.oopscraft.application.core.JsonUtils;
import net.oopscraft.application.user.User;
import net.oopscraft.application.user.UserRepository;
import net.oopscraft.application.user.mapper.UserMapper;

@Controller
@RequestMapping("/api/user")
public class UserController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private MessageSource messageSource;
 	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserMapper userMapper;
	
	/**
	 * Gets list of users.
	 * @param key
	 * @param value
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.GET, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@Transactional(rollbackFor = Exception.class)
	public ResponseEntity<?> getUsers(
		 @RequestParam(value="key",required=false)String key
		,@RequestParam(value="value",required=false)String value
	) throws Exception {
		
		List<User> users = null;
		if("id".equals(key)) {
			users = userRepository.findByIdLike(value);
		}else if("name".equals(key)) {
			users = userRepository.findByNameLike(value);
		}else {
			users = userRepository.findAll();
		}
		
//		for(String beanName : applicationContext.getBeanDefinitionNames()) {
//			Object beanObj = applicationContext.getBean(beanName);
//			System.out.println("+ beanName[" + beanName + "]:" + beanObj);
//		}
//		
//		// insert via JPA
//		User userJpa = new User();
//		userJpa.setId("userjpa");
//		userJpa.setName("userjpa");
//		userRepository.saveAndFlush(userJpa);
//		
//		// insert via MYBATIS
//		User userMybatis = new User();
//		userMybatis.setId("userMybatis");
//		userMybatis.setName("userMybatis");
//		userMapper.insertUser(userMybatis);
//		
//		// prints all users.
//		List<User> users = userMapper.selectUserList(new User());
//		LOGGER.info(TextTableBuilder.build(users));
//		
//		// throws Exception
//		if(1 == 1) {
//			throw new Exception();	
//		}
		
		// return response
		return new ResponseEntity<>(JsonUtils.toJson(users), HttpStatus.OK);
	}
	
	/**
	 * Gets user
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/{id}", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> getUser(@PathVariable("id")String id) throws Exception {
		User user = userRepository.findOne(id);
		if(user == null) {
			String message = String.format("User[%s] is not found.", id);
			message = messageSource.getMessage("warn.notFound", new String[]{"User",id}, message, LocaleContextHolder.getLocale());
			return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(JsonUtils.toJson(user), HttpStatus.OK);
	}
	
	/**
	 * Saves user
	 * @param payload
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/{id}", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> saveUser(@RequestBody String payload) throws Exception {
		User user = JsonUtils.toObject(payload, User.class);
		userRepository.saveAndFlush(user);
		user = userRepository.findOne(user.getId());
		return new ResponseEntity<>(JsonUtils.toJson(user), HttpStatus.OK);
	}
	
	/**
	 * Removes user
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> removeUser(@PathVariable("id")String id) throws Exception {
		User user = userRepository.findOne(id);
		if(user == null) {
			String message = String.format("User[%s] is not found.", id);
			message = messageSource.getMessage("warn.notFound", new String[]{"User",id}, message, LocaleContextHolder.getLocale());
			return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
		}
		userRepository.delete(user.getId());
		return new ResponseEntity<>(JsonUtils.toJson(user), HttpStatus.OK);
	}


}
