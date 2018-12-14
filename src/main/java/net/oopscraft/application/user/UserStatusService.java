package net.oopscraft.application.user;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.oopscraft.application.user.repository.UserStatusRepository;

@Service
public class UserStatusService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserStatusService.class);
	
	@Autowired
	UserStatusRepository userStatusRepository;
	
	public List<UserStatus> getUserStatuses() throws Exception {
		List<UserStatus> userStatusCds = userStatusRepository.findAllByOrderByDisplaySeqAsc();
		LOGGER.debug("{}",userStatusCds);
		return userStatusCds;
	}

}
