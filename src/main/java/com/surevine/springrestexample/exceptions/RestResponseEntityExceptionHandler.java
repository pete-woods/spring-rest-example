//  Copyright (c) 2018 Surevine Ltd.
//
//  Permission is hereby granted, free of charge, to any person
//  obtaining a copy of this software and associated documentation
//  files (the "Software"), to deal in the Software without
//  restriction, including without limitation the rights to use, copy,
//  modify, merge, publish, distribute, sublicense, and/or sell copies
//  of the Software, and to permit persons to whom the Software is
//  furnished to do so, subject to the following conditions:
//
//  The above copyright notice and this permission notice shall be
//  included in all copies or substantial portions of the Software.
//  
//  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
//  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
//  OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
//  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
//  HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
//  WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
//  FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
//  OTHER DEALINGS IN THE SOFTWARE.

package com.surevine.springrestexample.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.util.WebUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.surevine.springrestexample.exceptions.ApiRestError.ErrorType;

@ControllerAdvice
public class RestResponseEntityExceptionHandler {

	private ObjectMapper mapper = new ObjectMapper();

	@ExceptionHandler(value = { ApiPermissionDeniedException.class })
	protected ResponseEntity<Object> handleConflict(ApiPermissionDeniedException ex, WebRequest request)
			throws JsonProcessingException {

		return handleExceptionInternal(ex,
				mapper.writeValueAsString(new ApiRestError().message(ex.getMessage()).type(ErrorType.FORBIDDEN)),
				new HttpHeaders(), HttpStatus.FORBIDDEN, request);
	}

	@ExceptionHandler(value = { ApiArgumentException.class })
	protected ResponseEntity<Object> handleConflict(ApiArgumentException ex, WebRequest request)
			throws JsonProcessingException {

		return handleExceptionInternal(ex,
				mapper.writeValueAsString(new ApiRestError().message(ex.getMessage()).type(ex.getErrorType())),
				new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler(value = { IllegalArgumentException.class, IllegalStateException.class })
	protected ResponseEntity<Object> handleArgumentError(RuntimeException ex, WebRequest request)
			throws JsonProcessingException {

		return handleExceptionInternal(ex, mapper.writeValueAsString(new ApiRestError().message(ex.getMessage())),
				new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
			request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
		}
		return new ResponseEntity<>(body, headers, status);
	}
}