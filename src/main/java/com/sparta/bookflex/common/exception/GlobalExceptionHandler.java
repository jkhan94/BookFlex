package com.sparta.bookflex.common.exception;

import com.sparta.bookflex.common.exception.exceptionDto.EnvelopeResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
	 @ExceptionHandler(BusinessException.class)
	 public ResponseEntity<?> handleBusinessException(BusinessException ex, HttpServletRequest request) {
	 	return EnvelopeResponse.wrapError(
	 		ex.getStatus(),
	 		ex.getMessage(),
	 		request.getRequestURI()
	 	);
	 }

	 @ExceptionHandler
	 public ResponseEntity<?> handleBeanValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
	 	String errorMessage = ex.getBindingResult().getAllErrors().stream()
	 		.map(error -> {
	 			FieldError fieldError = (FieldError) error;
	 			return String.format("%s: %s", fieldError.getField(), fieldError.getDefaultMessage());
	 		}).collect(Collectors.joining("\n"));
	 	return EnvelopeResponse.wrapError(
	 		HttpStatus.BAD_REQUEST,
	 		errorMessage,
	 		request.getRequestURI()
	 	);
	 }

	 @ExceptionHandler
	 public ResponseEntity<?> handleUnhandledException(Exception e, HttpServletRequest request) {
	 	log.info("", e);
	 	return EnvelopeResponse.wrapError(
	 		HttpStatus.INTERNAL_SERVER_ERROR,
	 		HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
	 		request.getRequestURI()
	 	);
	 }

	 @ExceptionHandler(AuthenticationException.class)
	 public ResponseEntity<?> handleAuthenticationException(AuthenticationException ex) {
		 // Custom handling logic
		 return EnvelopeResponse.wrapError(
			 HttpStatus.UNAUTHORIZED,
			 ex.getMessage(),
			 null
		 );
	 }
}






