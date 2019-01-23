package net.oopscraft.application.core.spring;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalControllerAdvice {
	 @ExceptionHandler(Exception.class)
	 public ResponseEntity<?> handleException(final Exception e) {
		 return 
		 return error(e, HttpStatus.NOT_FOUND, e.getId().toString());
	 }
	 
}
