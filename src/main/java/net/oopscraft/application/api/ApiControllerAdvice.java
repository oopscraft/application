package net.oopscraft.application.api;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import net.oopscraft.application.message.MessageService;

@ControllerAdvice(basePackages = {"net.oopscraft.application.api"})
public class ApiControllerAdvice {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(ApiControllerAdvice.class);
	
	@Autowired
	HttpServletRequest request;
	
	@Autowired
	MessageService messageService;
	
	/**
	 * Handles exception
	 * @param exception
	 * @return
	 * @throws Exception
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> error(Exception exception) throws Exception {
		LOGGER.error(exception.getMessage(), exception);
		String message = ExceptionUtils.getRootCauseMessage(exception);
		return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}