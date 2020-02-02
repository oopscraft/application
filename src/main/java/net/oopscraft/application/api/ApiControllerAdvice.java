package net.oopscraft.application.api;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.oopscraft.application.core.JsonConverter;
import net.oopscraft.application.message.MessageService;
import net.oopscraft.application.util.LocaleUtility;

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
	
	
	/**
	 * getLocales
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "locales", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> getLocales() throws Exception {
		return new ResponseEntity<>(JsonConverter.toJson(LocaleUtility.getLocales()), HttpStatus.OK);
	}

	/**
	 * getLanguages
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="languages", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> getLanguages() throws Exception {
		return new ResponseEntity<>(JsonConverter.toJson(LocaleUtility.getLanguages()), HttpStatus.OK);
	}

	
}