package net.oopscraft.application.error;

import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;

import net.oopscraft.application.core.IdGenerator;
import net.oopscraft.application.core.JsonConverter;
import net.oopscraft.application.core.jpa.SystemEmbeddedException;
import net.oopscraft.application.message.MessageException;

@Controller
@ControllerAdvice
@RequestMapping("/error")
public class ErrorController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ErrorController.class);
	
	@Autowired
	ErrorService errorService;
	
	@Autowired
	LocaleResolver localeResolver;
	
	@Autowired
    MessageSource messageSource;
	
	/**
	 * error index
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int statusCode = Integer.parseInt(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE).toString());
		String message = statusCode + " " + HttpStatus.valueOf(statusCode).getReasonPhrase();
		Error error = createError(new ServletException(message), request);
		error.setStatusCode(statusCode);
		if(request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI) != null) {
			error.setRequestUri(request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI).toString());
		}
		return responseError(request, response, error);
	}
	
	/*
	 * createError
	 */
	private Error createError(Exception exception) throws Exception {
		Error error = new Error(IdGenerator.uuid());
		error.setDate(new Date());
		error.setExceptionClass(exception.getClass().getSimpleName());
		error.setExceptionMessage(exception.getMessage());
		error.setExceptionTrace(StringUtils.join(ExceptionUtils.getRootCauseStackTrace(exception),System.lineSeparator()));
		return error;
	}
	
	/*
	 * createError
	 */
	private Error createError(Exception exception, HttpServletRequest request) throws Exception {
		Error error = createError(exception);
		error.setRequestUri(request.getRequestURI());
		error.setRequestMethod(request.getMethod());
		error.setQueryString(request.getQueryString());
		return error;
	}
	
	/*
	 * responseError
	 */
	@Transactional
	private ModelAndView responseError(HttpServletRequest request, HttpServletResponse response, Error error) throws Exception {
		LOGGER.warn("{}", JsonConverter.toJson(error));
		ModelAndView modelAndView = null;
		if("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))
		|| error.getRequestUri().startsWith("/api/")
		){
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(JsonConverter.toJson(error));
			response.setStatus(error.getStatusCode());
		}else {
			modelAndView = new ModelAndView("error/error.html");
			modelAndView.addObject("error", error);
			modelAndView.setStatus(HttpStatus.valueOf(error.getStatusCode()));
		}
		
		// saves error into database.
		try {
			errorService.saveError(error);
		}catch(Exception ignore) {
			LOGGER.warn(ignore.getMessage(), ignore);
		}
		
		// returns modelAndView
		return modelAndView;
	}
	
	/**
	 * 500 - Default exception handler
	 * @param request
	 * @param response
	 * @param exception
	 * @throws Exception
	 */
	@ExceptionHandler(Exception.class)
	public ModelAndView handleException(HttpServletRequest request, HttpServletResponse response, Exception exception) throws Exception {
		Error error = createError(exception, request);
		error.setExceptionMessage(messageSource.getMessage("application.global.exception.Exception", null, localeResolver.resolveLocale(request)));
		error.setStatusCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		return responseError(request, response, error);
	}
	
	/**
	 * 403 - Handling Authorization Error
	 * @param request
	 * @param response
	 * @param exception
	 * @throws Exception
	 */
	@ExceptionHandler(AccessDeniedException.class)
	public ModelAndView handleAccessDeniedException(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception) throws Exception {
		Error error = createError(exception, request);
		error.setExceptionMessage(messageSource.getMessage("application.global.exception.AccessDeniedException", null, localeResolver.resolveLocale(request)));
		error.setStatusCode(HttpServletResponse.SC_FORBIDDEN);
		return responseError(request, response, error);
	}

	/**
	 * MethodArgumentNotValidException
	 * @param request
	 * @param response
	 * @param exception
	 * @throws Exception
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ModelAndView handleMethodArgumentNotValidException(HttpServletRequest request, HttpServletResponse response, MethodArgumentNotValidException exception) throws Exception {
		Error error = createError(exception, request);
		StringBuffer message = new StringBuffer();
		for(FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
			message.append(String.format("[%s.%s] %s", fieldError.getObjectName(), fieldError.getField(), fieldError.getDefaultMessage()));
			break;
		}
		error.setExceptionMessage(message.toString());
		error.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
		return responseError(request, response, error);
	}
	
	/**
	 * 403 - Handling SystemEmbeddedException
	 * @param request
	 * @param response
	 * @param exception
	 * @throws Exception
	 */
	@ExceptionHandler(SystemEmbeddedException.class)
	public ModelAndView handleSystemEmbeddedException(HttpServletRequest request, HttpServletResponse response, SystemEmbeddedException exception) throws Exception {
		Error error = createError(exception, request);
		error.setExceptionMessage(messageSource.getMessage("application.global.exception.SystemEmbeddedException", null, localeResolver.resolveLocale(request)));
		error.setStatusCode(HttpServletResponse.SC_FORBIDDEN);
		return responseError(request, response, error);
	}
	
	/**
	 * 400 - Handling Business Message Exception
	 * @param request
	 * @param response
	 * @param exception
	 * @throws Exception
	 */
	@ExceptionHandler(MessageException.class)
	public ModelAndView handleMessageException(HttpServletRequest request, HttpServletResponse response, MessageException exception) throws Exception {
		Error error = createError(exception, request);
		error.setExceptionMessage(exception.getMessage());
		error.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
		return responseError(request, response, error);
	}

}
