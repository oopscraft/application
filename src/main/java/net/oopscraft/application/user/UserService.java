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
import net.oopscraft.application.core.TextTable;
import net.oopscraft.application.user.repository.UserRepository;

@Service
public class UserService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

	public enum SearchKey {
		ID, NAME, EMAIL, PHONE
	}

	@Autowired
	UserRepository userRepository;

	/**
	 * Gets list of user by search condition and value
	 * 
	 * @param searchKey
	 * @param searchValue
	 * @return
	 * @throws Exception
	 */
	public List<User> getUsers(SearchKey searchKey, String searchValue, PageInfo pageInfo) throws Exception {
		Page<User> users = null;
		Pageable pageable = new PageRequest(pageInfo.getPage() - 1, pageInfo.getRows());
		if (searchKey == null) {
			users = userRepository.findAll(pageable);
		} else {
			switch (searchKey) {
			case ID:
				users = userRepository.findByIdLike(searchValue, pageable);
				break;
			case NAME:
				users = userRepository.findByNameLike(searchValue, pageable);
				break;
			case EMAIL:
				users = userRepository.findByEmailLike(searchValue, pageable);
				break;
			case PHONE:
				users = userRepository.findByPhoneLike(searchValue, pageable);
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
		User one = userRepository.findOne(user.getId());
		one.setName(user.getName());
		one.setNickname(user.getNickname());
		one.setEmail(user.getEmail());
		one.setPhone(user.getPhone());
		one.setPhoto(user.getPhoto());
		one.setMessage(user.getMessage());
		LOGGER.info("{}", new TextTable(one));
		userRepository.save(one);
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
