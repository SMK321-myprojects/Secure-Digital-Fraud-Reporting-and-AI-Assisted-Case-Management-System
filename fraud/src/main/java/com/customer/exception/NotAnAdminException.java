package com.customer.exception;

public class NotAnAdminException extends RuntimeException {
	
	public NotAnAdminException(String message)
	{
		super(message);
	}

}
