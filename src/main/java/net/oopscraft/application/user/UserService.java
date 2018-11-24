/*
 * Copyright since 2002 oopscraft.net
 *
 * Everyone is permitted to copy and distribute verbatim copies of this license document, 
 * but changing it is not allowed.
 * Released under the LGPL-3.0 licence
 * https://opensource.org/licenses/lgpl-3.0.html
 */
package net.oopscraft.application.user;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import net.oopscraft.application.core.PageInfo;
import net.oopscraft.application.core.TextTableBuilder;
import net.oopscraft.application.user.repository.UserRepository;

@Service
public class UserService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

	public enum FindBy {
		ID_LIKE, NAME_LIKE, EMAIL_LIKE, PHONE_LIKE
	}

	@Autowired
	UserRepository userRepository;

	/**
	 * Gets list of user by search condition and value
	 * 
	 * @param findBy
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public List<User> getUsers(FindBy findBy, String value, PageInfo pageInfo) throws Exception {
		Page<User> users = null;
		Pageable pageable = new PageRequest(pageInfo.getPage() - 1, pageInfo.getSize());
		if (findBy == null) {
			users = userRepository.findAll(pageable);
		} else {
			switch (findBy) {
			case ID_LIKE:
				users = userRepository.findByIdLike(value, pageable);
				break;
			case NAME_LIKE:
				users = userRepository.findByNameLike(value, pageable);
				break;
			case EMAIL_LIKE:
				users = userRepository.findByEmailLike(value, pageable);
				break;
			case PHONE_LIKE:
				users = userRepository.findByPhoneLike(value, pageable);
				break;
			}
		}
		if (pageInfo.isEnableTotalCount() == true) {
			pageInfo.setTotalCount(users.getTotalElements());
		}
		return users.getContent();
	}

	/**
	 * Gets user
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public User getUser(String id) throws Exception {
		User user = userRepository.findOne(id);
		return user;
	}

	/**
	 * Saves user details
	 * 
	 * @param user
	 * @throws Exception
	 */
	public User saveUser(User user) throws Exception {
		User prevUser = userRepository.findOne(user.getId());
		prevUser.setName(user.getName());
		LOGGER.info(TextTableBuilder.build(prevUser));
		userRepository.save(prevUser);
		return userRepository.findOne(user.getId());
	}

	/**
	 * Removes group details
	 * 
	 * @param id
	 * @throws Exception
	 */
	public User removeUser(String id) throws Exception {
		User persistUser = userRepository.findOne(id);
		userRepository.delete(id);
		return persistUser;
	}

}
