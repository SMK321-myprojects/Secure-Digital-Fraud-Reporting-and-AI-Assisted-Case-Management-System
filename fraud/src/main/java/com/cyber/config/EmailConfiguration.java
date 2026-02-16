package com.cyber.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration
public class EmailConfiguration
{
   @Value("${spring.mail.username}")
   private String from;
   @Autowired
   private JavaMailSender javaMailSender;
   
   public void userRegistrationMailFormat(String to, String name)
   {
	   SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
	   
	   simpleMailMessage.setFrom(from);
	   simpleMailMessage.setTo(to);
	   simpleMailMessage.setSubject("Registration Successful | Crime Registration Portal");

	   simpleMailMessage.setText(
	        "Hello " + name + ",\n\n" +
	        "Welcome to the Crime Registration Portal.\n\n" +
	        "Your account has been successfully created. You can now log in to the portal and:\n" +
	        "- Report crimes securely\n" +
	        "- Track the status of your complaints\n" +
	        "- View updates from authorities\n\n" +
	        "If you did not create this account, please contact our support team immediately.\n\n" +
	        "Regards,\n" +
	        "Crime Registration Portal Team\n" +
	        "This is an automated message. Please do not reply."
	    );
	   javaMailSender.send(simpleMailMessage);
	   
   }
   
   public void ticketGeneratedMailWithId(String to,String name,Long ticketId,String incidentType)
   {
	   SimpleMailMessage ticketMessage = new SimpleMailMessage();
	   
	   ticketMessage.setFrom(from);
	   ticketMessage.setTo(to);
	   ticketMessage.setSubject("Ticket Submitted Successfully | Crime Registration Portal");

	   ticketMessage.setText(
	        "Hello " + name + ",\n\n" +
	        "Your complaint has been successfully registered on the Crime Registration Portal.\n\n" +
	        "Ticket Details:\n" +
	        "Ticket ID       : " + ticketId + "\n" +
	        "Incident Type  : " + incidentType + "\n" +
	        "Status         : Reported\n\n" +
	        "Our team will review your complaint and update the status as it progresses.\n" +
	        "You can track your ticket anytime by logging into the portal.\n\n" +
	        "If this complaint was not submitted by you, please contact our support team immediately.\n\n" +
	        "Regards,\n" +
	        "Crime Registration Portal Team\n" +
	        "This is an automated message. Please do not reply."
	    );
	   
	   javaMailSender.send(ticketMessage);
   }
}
