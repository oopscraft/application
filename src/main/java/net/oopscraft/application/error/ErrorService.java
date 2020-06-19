package net.oopscraft.application.error;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ErrorService {
	
	@Autowired
	ErrorRepository errorRepository;
	
	/**
	 * saveError
	 * @param error
	 * @return
	 * @throws Exception
	 */
	public void saveError(Error error) throws Exception {
		errorRepository.saveAndFlush(error);
	}
	
}
