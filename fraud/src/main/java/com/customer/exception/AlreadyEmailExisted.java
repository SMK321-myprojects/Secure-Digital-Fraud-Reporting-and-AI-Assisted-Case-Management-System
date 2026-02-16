package com.customer.exception;

public class AlreadyEmailExisted extends RuntimeException{
   public AlreadyEmailExisted(String message)
   {
	   super(message);
   }
}
