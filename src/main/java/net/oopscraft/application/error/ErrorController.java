package net.oopscraft.application.error;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import net.oopscraft.application.core.JsonConverter;
import net.oopscraft.application.message.MessageException;

@Controller
@ControllerAdvice
@RequestMapping("/error")
public class ErrorController {
	
	@Autowired
	ErrorService errorService;
	
	@Autowired
	LocaleResolver localeResolver;
	
	@Autowired
    MessageSource messageSource;
	
	/**
	 * responseErrorMessage
	 * @param response
	 * @param errorMessage
	 * @param status
	 * @throws Exception
	 */
	private ModelAndView responseError(HttpServletRequest request, HttpServletResponse response, Error error) throws Exception {
		errorService.saveError(error);
		if("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))
		|| error.getUri().startsWith("/api/")
		){
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(JsonConverter.toJson(error));
			response.setStatus(error.getStatusCode());
			return null;
		}else {
			ModelAndView modelAndView = new ModelAndView("error/error.html");
			modelAndView.addObject("error", error);
			modelAndView.setStatus(HttpStatus.valueOf(error.getStatusCode()));
			return modelAndView;
		}
	}
	
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
		Error error = errorService.createError(new ServletException(message), request);
		error.setStatusCode(statusCode);
		if(request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI) != null) {
			error.setUri(request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI).toString());
		}
		return responseError(request, response, error);
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
		Error error = errorService.createError(exception, request);
		error.setMessage(messageSource.getMessage("application.global.exception.Exception", null, localeResolver.resolveLocale(request)));
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
		Error error = errorService.createError(exception, request);
		error.setMessage(messageSource.getMessage("application.global.exception.AccessDeniedException", null, localeResolver.resolveLocale(request)));
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
		Error error = errorService.createError(exception, request);
		StringBuffer message = new StringBuffer();
		for(FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
			message.append(String.format("[%s.%s] %s", fieldError.getObjectName(), fieldError.getField(), fieldError.getDefaultMessage()));
			break;
		}
		error.setMessage(message.toString());
		error.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
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
		Error error = errorService.createError(exception, request);
		error.setStatusCode(HttpServletResponse.SC_BAD_REQUEST);
		// TODO get custom message
		return responseError(request, response, error);
	}

}
