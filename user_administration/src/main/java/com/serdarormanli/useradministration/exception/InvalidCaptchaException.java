package com.serdarormanli.useradministration.exception;

public class InvalidCaptchaException extends Exception {

	private static final long serialVersionUID = -5012439374505926924L;

	public InvalidCaptchaException(String message) {
		super(message);
	}
}
