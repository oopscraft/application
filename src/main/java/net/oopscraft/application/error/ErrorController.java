package net.oopscraft.application.error;

import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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

import net.oopscraft.application.core.JsonConverter;
import net.oopscraft.application.core.ValueMap;
import net.oopscraft.application.message.MessageException;

@Controller
@ControllerAdvice
@RequestMapping("/error")
public class ErrorController {
	
	@Autowired
	LocaleResolver localeResolver;
	
	@Autowired
    MessageSource messageSource;
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView modelAndView = new ModelAndView("error/error.html");
		int statusCode = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE) == null ? 0 
						: Integer.parseInt(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE).toString());
		modelAndView.addObject("statusCode", statusCode);
		String message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE) == null	? ""
						: request.getAttribute(RequestDispatcher.ERROR_MESSAGE).toString();
		modelAndView.addObject("message", message);
		return modelAndView;
	}
	
	
	/**
	 * createDefaultErrorMessage
	 * @param request
	 * @param exception
	 * @return
	 * @throws Exception
	 */
	private ValueMap createDefaultErrorMessage(HttpServletRequest request, Exception exception) throws Exception {
		ValueMap errorMessage = new ValueMap();
		errorMessage.set("uri", request.getRequestURI());
		errorMessage.set("method", request.getMethod());
		errorMessage.set("timestamp", new Date());
		errorMessage.set("exception", exception.getClass().getSimpleName());
		errorMessage.set("message", ExceptionUtils.getRootCauseMessage(exception));
		return errorMessage;
	}
	
	/**
	 * responseErrorMessage
	 * @param response
	 * @param errorMessage
	 * @param status
	 * @throws Exception
	 */
	private void responseErrorMessage(HttpServletResponse response, ValueMap errorMessage, int status) throws Exception {
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(JsonConverter.toJson(errorMessage));
		response.setStatus(status);
	}
	
	/**
	 * 500 - Default exception handler
	 * @param request
	 * @param response
	 * @param exception
	 * @throws Exception
	 */
	@ExceptionHandler(Exception.class)
	public void handleException(HttpServletRequest request, HttpServletResponse response, Exception exception) throws Exception {
		ValueMap errorMessage = createDefaultErrorMessage(request, exception);
		errorMessage.setString("message", messageSource.getMessage("application.global.exception.Exception", null, localeResolver.resolveLocale(request)));
		if("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))){
			responseErrorMessage(response, errorMessage, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}else {
			throw exception;
		}
	}
	
	/**
	 * 403 - Handling Authorization Error
	 * @param request
	 * @param response
	 * @param exception
	 * @throws Exception
	 */
	@ExceptionHandler(AccessDeniedException.class)
	public void handleAccessDeniedException(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception) throws Exception {
		ValueMap errorMessage = createDefaultErrorMessage(request, exception);
		errorMessage.setString("message", messageSource.getMessage("application.global.exception.AccessDeniedException", null, localeResolver.resolveLocale(request)));
		if("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))){
			responseErrorMessage(response, errorMessage, HttpServletResponse.SC_FORBIDDEN);
		}else {
			throw exception;
		}
	}

	/**
	 * MethodArgumentNotValidException
	 * @param request
	 * @param response
	 * @param exception
	 * @throws Exception
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public void handleMethodArgumentNotValidException(HttpServletRequest request, HttpServletResponse response, MethodArgumentNotValidException exception) throws Exception {
		ValueMap errorMessage = createDefaultErrorMessage(request, exception);
		StringBuffer message = new StringBuffer();
		if("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))){
			for(FieldError error : exception.getBindingResult().getFieldErrors()) {
				message.append(String.format("[%s.%s] %s", error.getObjectName(), error.getField(), error.getDefaultMessage()));
				break;
			}
			errorMessage.setString("message", message);
			responseErrorMessage(response, errorMessage, HttpServletResponse.SC_BAD_REQUEST);
		}else {
			throw exception;
		}
	}
	
	/**
	 * 400 - Handling Business Message Exception
	 * @param request
	 * @param response
	 * @param exception
	 * @throws Exception
	 */
	@ExceptionHandler(MessageException.class)
	public void handleMessageException(HttpServletRequest request, HttpServletResponse response, MessageException exception) throws Exception {
		ValueMap errorMessage = createDefaultErrorMessage(request, exception);
		// TODO get custom message
		if("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))){
			responseErrorMessage(response, errorMessage, HttpServletResponse.SC_BAD_REQUEST);
		}else {
			throw exception;
		}
	}

}
