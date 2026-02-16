package com.cyber.service;

import org.springframework.stereotype.Service;

@Service
public class ChatService {

    public String generateReply(String message) {
    	
        if (message == null || message.isBlank()) {
            return "Please enter a valid message.";
        }

        String lower = message.toLowerCase();

        if (lower.contains("ticket") || lower.contains("report") || lower.contains("raise")) {
            return "You can raise a new ticket from the 'report new ticket' option on the sidebar of this page then report page will open.";
        }

        if (lower.contains("status") || lower.contains("check") || lower.contains("my tickets") ||lower.contains("all tickets")) {
            return "You can check ticket status in the view all tickets option then my tickets page will open.";
        }

        if (lower.contains("hello") || lower.contains("hi")) {
            return "Hello! How can I assist you today?";
        }
        
        if(lower.contains("info") || lower.contains("awareness page") || lower.contains("news"))
        {
        	return "there is an option in sidebar called awareness just click the button";
        }
        
        if(lower.contains("recent tickets") || lower.contains("recent"))
        {
        	return "Currently the recent page is unavailable just wait for some other time";
        }
        return "Please describe your issue clearly so I can help.";
    }
}
