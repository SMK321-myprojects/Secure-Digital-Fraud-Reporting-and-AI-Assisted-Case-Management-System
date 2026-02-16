package com.cyber.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.customer.exception.AdminInvalidCredentials;
import com.customer.exception.AdminNotFoundException;
import com.cyber.model.Role;
import com.cyber.model.Ticket;
import com.cyber.model.TicketStatus;
import com.cyber.model.User;
import com.cyber.repositories.TicketRepository;
import com.cyber.repositories.UserRepository;

@Service
public class AdminService
{
	@Autowired
	private TicketRepository ticketRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;


	public List<Ticket> getAllTickets() {
	return ticketRepository.findAllByOrderByCreatedAtDesc();
	}


	public void updateTicketStatus(Long ticketId, TicketStatus status) {
	Ticket ticket = ticketRepository.findById(ticketId)
	.orElseThrow(() -> new RuntimeException("Ticket not found"));


	ticket.setStatus(status);
	ticketRepository.save(ticket);
	}



	public User login(String email, String password) {

	    User user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new AdminNotFoundException("Admin not found"));

	    if (user.getRole() != Role.ADMIN) {
	        throw new RuntimeException("Unauthorized");
	    }

	    if (!passwordEncoder.matches(password, user.getPassword())) {
	        throw new AdminInvalidCredentials("Invalid password");
	    }

	    return user;
	}

}
