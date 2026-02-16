package com.cyber.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cyber.config.EmailConfiguration;
import com.cyber.model.Ticket;
import com.cyber.service.TicketService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/tickets")
public class TicketController
{
	@Autowired
    private TicketService ticketService;
	
	
	
	@PostMapping("/ticket/create")
	public String createTicket(Ticket ticket,
	                           HttpSession session) {

	    Long userId = (Long) session.getAttribute("loggedUserId");

	    if (userId == null)
	    {
	        return "redirect:/login";
	    }

	    ticketService.createTicket(userId, ticket);
	    
	    return "redirect:/mytickets";
	}

	
	@GetMapping("/user/{userId}")
	public List<Ticket> getUserTickets(@PathVariable Long userId)
	{
	    return ticketService.getTicketsByUserId(userId);
	}
}
