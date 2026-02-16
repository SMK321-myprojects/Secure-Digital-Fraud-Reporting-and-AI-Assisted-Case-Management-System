package com.cyber.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cyber.service.ChatService;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
	

    @Autowired
    private ChatService chatService;

    @PostMapping
    public ResponseEntity<String> chat(@RequestBody Map<String, String> request) {

        String userMessage = request.get("message");

        // For now simple response
        String botReply = chatService.generateReply(userMessage);

        return ResponseEntity.ok(botReply);
    }

   
}

