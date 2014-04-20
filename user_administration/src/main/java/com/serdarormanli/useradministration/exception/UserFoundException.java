package com.serdarormanli.useradministration.exception;

public class UserFoundException extends RuntimeException {

	private static final long serialVersionUID = 4322764103631590100L;

	public UserFoundException() {
		super();
	}

	public UserFoundException(String message) {
		super(message);
	}
}
