package com.serdarormanli.useradministration.exception;

/***
 * Exception for user not found exception
 * 
 * @author Serdar ORMANLI
 *
 */
public class UserFoundException extends RuntimeException {

	private static final long serialVersionUID = 4322764103631590100L;

	public UserFoundException() {
		super();
	}

	public UserFoundException(String message) {
		super(message);
	}
}
