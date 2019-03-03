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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import net.oopscraft.application.core.PageInfo;
import net.oopscraft.application.core.StringUtils;
import net.oopscraft.application.user.repository.UserRepository;

@Service
public class UserService {
	
	public enum UserSearchType { ID, NAME, EMAIL, PHONE	}

	@Autowired
	UserRepository userRepository;
	
	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	/**
	 * Gets list of user by search condition and value
	 * @param pageInfo
	 * @param searchType
	 * @param searchValue
	 * @return
	 * @throws Exception
	 */
	public List<User> getUsers(PageInfo pageInfo, UserSearchType searchType, String searchValue) throws Exception {
		Pageable pageable = pageInfo.toPageable();
		Page<User> usersPage = null;
		if(searchType == null) {
			usersPage = userRepository.findAll(pageable);
		}else {
			switch(searchType) {
				case ID :
					usersPage = userRepository.findByIdStartingWith(searchValue, pageable);
				break;
				case NAME :
					usersPage = userRepository.findByNameStartingWith(searchValue, pageable);
				break;
				case EMAIL: 
					usersPage = userRepository.findByEmailStartingWith(searchValue, pageable);
				break;
				case PHONE:
					usersPage = userRepository.findByPhoneStartingWith(searchValue, pageable);
				break;
			}
		}
		if (pageInfo.isEnableTotalCount() == true) {
			pageInfo.setTotalCount(usersPage.getTotalElements());
		}
		List<User> users = usersPage.getContent();
		return users;
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
		
		// checks validation
		if(StringUtils.isEmpty(user.getId())) {
			throw new IllegalArgumentException("invalid id");
		}
		if(StringUtils.isEmpty(user.getName())) {
			throw new IllegalArgumentException("invalid name");
		}
		if(StringUtils.isEmpty(user.getNickname())) {
			throw new IllegalArgumentException("invalid nickname");
		}
		if(StringUtils.isEmpty(user.getEmail())) {
			throw new IllegalArgumentException("invalid email");
		}
		
		// In case of new user
		User one = userRepository.findOne(user.getId());
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
		one.setStatus(user.getStatus());
		one.setLocale(user.getLocale());
		one.setPhone(user.getPhone());
		
		// AVATAR property
		if(user.getAvatar() != null) {
			if(user.getAvatar().length() > 1024*1024) {
				throw new IllegalArgumentException("Avatar image size exceeds the limit.");
			}
		}
		one.setAvatar(user.getAvatar());
		
		// Signature property
		if(user.getSignature() != null) {
			if(user.getSignature().length() > 1024*1024) {
				throw new IllegalArgumentException("Signature size exceeds the limit.");
			}
		}
		one.setSignature(user.getSignature());
		
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
		
		return userRepository.save(one);
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
	 * Removes user
	 * 
	 * @param id
	 * @throws Exception
	 */
	public void deleteUser(String id) throws Exception {
		User user = userRepository.findOne(id);
		if(user != null) {
			userRepository.delete(user);
		}
	}

}
