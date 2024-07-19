package com.sparta.bookflex.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class BusinessException extends RuntimeException {
	private final HttpStatus status;
	private final String message;

	public BusinessException(ErrorCode errorCode) {
		this.status = errorCode.getStatus();
		this.message = errorCode.getMessage();
	}
}