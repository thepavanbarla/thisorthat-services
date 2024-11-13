package com.tot.exception;

import com.amazonaws.services.pinpoint.model.ForbiddenException;

public class UnauthorizedAccessException extends ForbiddenException{

	private static final long serialVersionUID = 612823967302464252L;

	public UnauthorizedAccessException(String message) {
		super(message);
	}

}
