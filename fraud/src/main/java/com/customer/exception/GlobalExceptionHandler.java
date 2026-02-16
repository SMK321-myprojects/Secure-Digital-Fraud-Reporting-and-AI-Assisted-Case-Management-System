package com.customer.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler
{
	@ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFound(UserNotFoundException ex, RedirectAttributes redirectAttributes) {
		System.out.println("Handler triggered: " + ex.getMessage());
		redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return "redirect:/login";
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public String handleInvalidCredentials(InvalidCredentialsException ex, RedirectAttributes redirectAttributes) {
    	redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return "redirect:/login";
    }
    
    @ExceptionHandler(AdminNotFoundException.class)
    public String handleAdminNotFound(AdminNotFoundException an,RedirectAttributes redirectAttributes)
    {
    	redirectAttributes.addFlashAttribute("error", an.getMessage());
    	return "redirect:/adminlogin";
    }

    @ExceptionHandler(AdminInvalidCredentials.class)
    public String handleAdminInvalidCredentials(AdminInvalidCredentials ai,RedirectAttributes redirectAttributes)
    {
    	redirectAttributes.addFlashAttribute("error", ai.getMessage());
    	return "redirect:/adminlogin";
    }

    @ExceptionHandler(AlreadyEmailExisted.class)
    public String handleAlreadyEmailExisted(AlreadyEmailExisted ae,RedirectAttributes redirectAttributes)
    {
    	redirectAttributes.addFlashAttribute("error", ae.getMessage());
    	return "redirect:/register";
    }
    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception ex, RedirectAttributes redirectAttributes) {
    	redirectAttributes.addFlashAttribute("error", "Something went wrong. Please try again.");
        return "error";
    }
    
  
    
    
} 
