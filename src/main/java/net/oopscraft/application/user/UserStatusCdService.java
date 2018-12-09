package net.oopscraft.application.user;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.oopscraft.application.user.repository.UserStatusCdRepository;

@Service
public class UserStatusCdService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserStatusCdService.class);
	
	@Autowired
	UserStatusCdRepository userStatusCdRepository;
	
	public List<UserStatusCd> getUserStatusCds() throws Exception {
		List<UserStatusCd> userStatusCds = userStatusCdRepository.findAllByOrderByDisplaySeqAsc();
		LOGGER.debug("{}",userStatusCds);
		return userStatusCds;
	}

}
