package com.customer.exception;

/*For invalid credentials*/
public class AdminInvalidCredentials extends RuntimeException {
   public AdminInvalidCredentials(String message)
   {
	   super(message);
   }
}
