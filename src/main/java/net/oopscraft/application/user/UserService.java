/*
 * Copyright since 2002 oopscraft.net
 *
 * Everyone is permitted to copy and distribute verbatim copies of this license document, 
 * but changing it is not allowed.
 * Released under the LGPL-3.0 licence
 * https://opensource.org/licenses/lgpl-3.0.html
 */
package net.oopscraft.application.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import net.oopscraft.application.core.PageInfo;
import net.oopscraft.application.core.TextTable;
import net.oopscraft.application.user.repository.UserRepository;

@Service
public class UserService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

	@Autowired
	UserRepository userRepository;
	
	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	/**
	 * Search condition class
	 *
	 */
	public class SearchCondition {
		String id;
		String name;
		String email;
		String phone;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}
	}


	/**
	 * Gets list of user by search condition and value
	 * 
	 * @param searchKey
	 * @param searchValue
	 * @return
	 * @throws Exception
	 */
	public List<User> getUsers(SearchCondition searchCondition, PageInfo pageInfo) throws Exception {
		List<User> users = null;
		Page<User> page = null;
		Pageable pageable = new PageRequest(pageInfo.getPage() - 1, pageInfo.getRows());
		if (!StringUtils.isEmpty(searchCondition.getId())) {
			page = userRepository.findByIdStartingWith(searchCondition.getId(), pageable);
		} else if (!StringUtils.isEmpty(searchCondition.getId())) {
			page = userRepository.findByNameStartingWith(searchCondition.getName(), pageable);
		} else if (!StringUtils.isEmpty(searchCondition.getEmail())) {
			page = userRepository.findByEmailStartingWith(searchCondition.getName(), pageable);
		} else if (!StringUtils.isEmpty(searchCondition.getPhone())) {
			page = userRepository.findByPhoneStartingWith(searchCondition.getName(), pageable);
		} else {
			page = userRepository.findAllByOrderByJoinDateDesc(pageable);
		}
		users = page.getContent();
		if (pageInfo.isEnableTotalCount() == true) {
			pageInfo.setTotalCount(page.getTotalElements());
		}
		LOGGER.info("+ users: {}", new TextTable(users));
		return page.getContent();
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
		
		// In case of new user
		if(one == null) {
			one = new User();
			one.setId(user.getId());
			one.setJoinDate(new Date());
			one.setGroups(new ArrayList<Group>());
			one.setRoles(new ArrayList<Role>());
			one.setAuthorities(new ArrayList<Authority>());
			
			// Encodes password
			one.setPassword(passwordEncoder.encode(user.getPassword()));
		}
		
		one.setName(user.getName());
		one.setNickname(user.getNickname());
		one.setEmail(user.getEmail());
		one.setPhone(user.getPhone());
		
		// AVATAR property
		if(user.getAvatar() != null) {
			if(user.getAvatar().length() > 1024*1024) {
				throw new Exception("Avatar image size exceeds the limit.");
			}
		}
		one.setAvatar(user.getAvatar());
		
		// Signature property
		if(user.getSignature() != null) {
			if(user.getSignature().length() > 1024*1024) {
				throw new Exception("Signature size exceeds the limit.");
			}
		}
		one.setSignature(user.getSignature());
		
		// Profile property
		if(user.getProfile() != null) {
			if(user.getProfile().length() > 1024*1024) {
				throw new Exception("Profile size exceeds the limit.");
			}
		}
		one.setProfile(user.getProfile());
		
		// add groups
		one.getGroups().clear();
		for (Group group : user.getGroups()) {
			one.getGroups().add(group);
		}
		
		// add authorities
		one.getAuthorities().clear();
		for (Authority authority : user.getAuthorities()) {
			one.getAuthorities().add(authority);
		}
		
		// add authorities
		one.getRoles().clear();
		for (Role role : user.getRoles()) {
			one.getRoles().add(role);
		}
		
		userRepository.save(one);
		return userRepository.findOne(user.getId());
	}
	
	/**
	 * Verify Password
	 * @param id
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public boolean isValidPassword(String id, String password) {
		
		// gets user data
		User one = userRepository.findOne(id);
		if(one == null) {
			 return false;
		}
		// checking current password
		if(passwordEncoder.matches(password, one.getPassword()) == true) {
			return true;
		}
		return false;
	}
	
	/**
	 * Changes password
	 * @param id
	 * @param currentPassword
	 * @param newPassword
	 * @throws Exception
	 */
	public void changePassword(String id, String currentPassword, String newPassword) throws Exception {
		
		// checking current password
		if(isValidPassword(id, currentPassword) == false) {
			throw new Exception("Current password is invalid.");
		}
		
		// Updates new password
		User one = userRepository.findOne(id);
		one.setPassword(passwordEncoder.encode(newPassword));
		
		// Saves user
		userRepository.save(one);
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
