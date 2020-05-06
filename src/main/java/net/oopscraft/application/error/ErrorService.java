package net.oopscraft.application.error;

import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.oopscraft.application.core.IdGenerator;

@Service
public class ErrorService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ErrorService.class);
	
	@Autowired
	ErrorRepository errorRepository;
	
	/**
	 * createError
	 * @param exception
	 * @return
	 * @throws Exception
	 */
	public Error createError(Exception exception) throws Exception {
		Error error = new Error(IdGenerator.uuid());
		error.setDate(new Date());
		error.setException(exception.getClass().getSimpleName());
		error.setMessage(exception.getMessage());
		error.setTrace(StringUtils.join(ExceptionUtils.getRootCauseStackTrace(exception),System.lineSeparator()));
		return error;
	}
	
	/**
	 * createError
	 * @param exception
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public Error createError(Exception exception, HttpServletRequest request) throws Exception {
		Error error = createError(exception);
		error.setUri(request.getRequestURI());
		error.setMethod(request.getMethod());
		error.setQueryString(request.getQueryString());
		return error;
	}
	
	/**
	 * saveError
	 * @param error
	 * @return
	 * @throws Exception
	 */
	public void saveError(Error error) {
		try {
			errorRepository.saveAndFlush(error);
		}catch(Exception ignore) {
			LOGGER.warn(ignore.getMessage());
		}
	}
	
}
