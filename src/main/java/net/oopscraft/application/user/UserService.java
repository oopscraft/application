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

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import net.oopscraft.application.core.Pagination;
import net.oopscraft.application.core.jpa.SystemEmbeddedException;
import net.oopscraft.application.user.entity.User;

@Service
public class UserService {
	
	@Autowired
	UserRepository userRepository;
	
	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	public static void main(String[] args) throws Exception {
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String password = passwordEncoder.encode("admin");
		System.out.println(password);
	}
	
	/**
	 * Return users.
	 * @param user
	 * @param pagination
	 * @return
	 * @throws Exception
	 */
	public List<User> getUsers(final User user, Pagination pagination) throws Exception {
		Page<User> usersPage = userRepository.findAll(new  Specification<User>() {
			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				if(user.getId() != null) {
					Predicate predicate = criteriaBuilder.and(criteriaBuilder.like(root.get("id").as(String.class), user.getId() + '%'));
					predicates.add(predicate);
				}
				if(user.getName() != null) {
					Predicate predicate = criteriaBuilder.and(criteriaBuilder.like(root.get("name").as(String.class), user.getName() + '%'));
					predicates.add(predicate);
				}
				if(user.getEmail() != null) {
					Predicate predicate = criteriaBuilder.and(criteriaBuilder.like(root.get("email").as(String.class), user.getEmail() + '%'));
					predicates.add(predicate);
				}
				if(user.getStatus() != null) {
					Predicate predicate = criteriaBuilder.and(criteriaBuilder.equal(root.get("status"), user.getStatus()));
					predicates.add(predicate);
				}
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));	
			}
		}, pagination.toPageRequest());
		pagination.setTotalCount(usersPage.getTotalElements());
		return usersPage.getContent();
	}
	
	/**
	 * Gets user
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public User getUser(String id) throws Exception {
		return userRepository.findOne(id);
	}
	
	/**
	 * Saves user.
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public User saveUser(User user) throws Exception {
		User one = userRepository.findOne(user.getId());
		if(one == null) {
			one = new User(user.getId());
			one.setPassword(passwordEncoder.encode(user.getPassword()));
			one.setJoinDate(new Date());
		}
		one.setName(user.getName());
		one.setNickname(user.getNickname());
		one.setStatus(user.getStatus());
		one.setEmail(user.getEmail());
		one.setMobileCountry(user.getMobileCountry());
		one.setMobileNumber(user.getMobileNumber());
		one.setPhoto(user.getPhoto());
		one.setProfile(user.getProfile());
		one.setLanguage(user.getLanguage());
		one.setCloseDate(user.getCloseDate());
		one.setGroups(user.getGroups());
		one.setRoles(user.getRoles());
		one.setAuthorities(user.getAuthorities());
		return userRepository.save(one);
	}
	
	/**
	 * Deletes user
	 * @param user
	 * @throws Exception
	 */
	public void deleteUser(User user) throws Exception {
		User one = userRepository.findOne(user.getId());
		if(one.isSystemEmbedded()) {
			throw new SystemEmbeddedException();
		}
		userRepository.delete(one);
	}
	
	/**
	 * Verifies Password
	 * @param id
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public boolean isCorrectPassword(String id, String password) {
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
	public void changePassword(String id, String password) throws Exception {
		User one = userRepository.findOne(id);
		if(one != null) {
			one.setPassword(passwordEncoder.encode(password));
			userRepository.save(one);
		}
	}

	
	


}
