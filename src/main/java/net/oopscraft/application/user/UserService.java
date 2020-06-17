/*
 * Copyright since 2002 oopscraft.net
 *
 * Everyone is permitted to copy and distribute verbatim copies of this license document, 
 * but changing it is not allowed.
 * Released under the LGPL-3.0 licence
 * https://opensource.org/licenses/lgpl-3.0.html
 */
package net.oopscraft.application.user;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.xml.bind.DatatypeConverter;

import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.imgscalr.Scalr.Mode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import net.oopscraft.application.core.IdGenerator;
import net.oopscraft.application.core.Pagination;
import net.oopscraft.application.core.jpa.SystemEmbeddedException;

@Service
public class UserService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	VerificationService verificationService;
	
	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	/**
	 * Return users.
	 * @param user
	 * @param pagination
	 * @return
	 * @throws Exception
	 */
	public List<User> getUsers(final User user, Pagination pagination) throws Exception {
		List<Order> orders = new ArrayList<Order>();
		orders.add(new Order(Direction.DESC, "systemInsertDate"));
		PageRequest pageRequest = pagination.toPageRequest(new Sort(orders));
		Page<User> usersPage = userRepository.findAll(new  Specification<User>() {
			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				if(user.getEmail() != null) {
					Predicate predicate = criteriaBuilder.and(criteriaBuilder.like(root.get("email").as(String.class), '%' + user.getEmail() + '%'));
					predicates.add(predicate);
				}
				if(user.getName() != null) {
					Predicate predicate = criteriaBuilder.and(criteriaBuilder.like(root.get("name").as(String.class), '%' + user.getName() + '%'));
					predicates.add(predicate);
				}
				if(user.getStatus() != null) {
					Predicate predicate = criteriaBuilder.and(criteriaBuilder.equal(root.get("status"), user.getStatus()));
					predicates.add(predicate);
				}
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));	
			}
		}, pageRequest);
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
	 * Gets user by email
	 * @param email
	 * @return
	 * @throws Exception
	 */
	public User getUserByEmail(String email) throws Exception {
		return userRepository.findByEmail(email);
	}
	
	/**
	 * Saves user.
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public User saveUser(User user) throws Exception {
		User one = null;
		if(user.getId() == null || (one = userRepository.findOne(user.getId())) == null) {
			one = new User(IdGenerator.uuid());
			one.setPassword(passwordEncoder.encode(user.getPassword()));
			one.setJoinDate(new Date());
		}
		one.setName(user.getName());
		one.setStatus(user.getStatus());
		if(user.getStatus() == User.Status.CLOSED) {
			one.setCloseDate(new Date());
		}else {
			one.setCloseDate(null);
		}
		one.setEmail(user.getEmail());
		one.setMobileCountry(user.getMobileCountry());
		one.setMobileNumber(user.getMobileNumber());
		one.setPhoto(user.getPhoto());
		one.setProfile(user.getProfile());
		one.setLanguage(user.getLanguage());
		one.setGroups(user.getGroups());
		one.setRoles(user.getRoles());
		one.setAuthorities(user.getAuthorities());
		
		// creates thumb nail
		if(one.getPhoto() != null) {
			ByteArrayInputStream bais = null;
			ByteArrayOutputStream baos = null;
			try {
				String photoDataUrl = one.getPhoto();
				String encodingPrefix = "base64,";
				int contentStartIndex = photoDataUrl.indexOf(encodingPrefix) + encodingPrefix.length();
				byte[] imageData = DatatypeConverter.parseBase64Binary(photoDataUrl.substring(contentStartIndex));
				bais = new ByteArrayInputStream(imageData);
				baos = new ByteArrayOutputStream();
				BufferedImage image = ImageIO.read(bais);
				BufferedImage thumbImg = Scalr.resize(image, Method.ULTRA_QUALITY, Mode.AUTOMATIC, 32, 32);
				ImageIO.write(thumbImg, "PNG", baos);
				byte[] encodeBase64 = Base64.getEncoder().encode(baos.toByteArray());
				String base64Encoded = new String(encodeBase64);
				String thumbnailDataUrl = "data:image/png;base64,"+base64Encoded;
				one.setThumbnail(thumbnailDataUrl);
			}catch(Exception ignore) {
				LOGGER.warn(ignore.getMessage());
			}finally {
				if(bais != null) try { bais.close(); }catch(Exception ignore){}
				if(baos != null) try { baos.close(); }catch(Exception ignore){}
			}
		}else {
			one.setThumbnail(null);
		}

		// save and return
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
	public boolean isCorrectPassword(String id, String password) throws Exception {
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
