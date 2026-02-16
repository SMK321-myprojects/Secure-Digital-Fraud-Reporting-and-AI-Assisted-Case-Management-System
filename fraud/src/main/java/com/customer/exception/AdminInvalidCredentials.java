package com.customer.exception;

public class AdminInvalidCredentials extends RuntimeException {
   public AdminInvalidCredentials(String message)
   {
	   super(message);
   }
}
