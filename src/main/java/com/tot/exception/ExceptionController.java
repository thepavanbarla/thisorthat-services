package com.tot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

	@ExceptionHandler(value = UnauthorizedAccessException.class)
	   public ResponseEntity<Object> exception(UnauthorizedAccessException exception) {
	      return new ResponseEntity<>("Token invalid or expired", HttpStatus.FORBIDDEN);
	   }
	
}
