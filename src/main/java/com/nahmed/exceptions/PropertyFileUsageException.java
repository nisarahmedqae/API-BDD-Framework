package com.nahmed.exceptions;

@SuppressWarnings("serial")
public class PropertyFileUsageException extends RuntimeException {

	public PropertyFileUsageException(String message) {
		super(message);
	}

	public PropertyFileUsageException(String message, Throwable cause) {
		super(message, cause);
	}

}
