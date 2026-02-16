package com.cyber.service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cyber.model.Ticket;
import com.cyber.model.TicketMedia;
import com.cyber.model.TicketStatus;
import com.cyber.model.User;
import com.cyber.repositories.TicketMediaRepository;
import com.cyber.repositories.TicketRepository;
import com.cyber.repositories.UserRepository;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

@Service
public class TicketService
{
	@Autowired
    private TicketRepository ticketRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TicketMediaRepository ticketMediaRepository;
	
	public Ticket createTicket(Long userId,Ticket ticket)
	{
		 User user = userRepository.findById(userId)
	                .orElseThrow(() -> new RuntimeException("User not found"));

	        ticket.setUser(user);
	        ticket.setStatus(TicketStatus.REPORTED);

	        return ticketRepository.save(ticket);
	}
	
	public List<Ticket> getTicketsByUserId(Long userId) {
	    return ticketRepository.findByUser_Id(userId);
	}
	
	public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id).orElse(null);
    }
	
	public Ticket getTicketByIdAndUserId(Long ticketId, Long userId) {
	    return ticketRepository.findById(ticketId)
	            .filter(t -> t.getUser().getId().equals(userId))
	            .orElse(null);
	}
	public void saveTicketMedia(Ticket ticket, MultipartFile[] files) throws IOException {

	    if (files == null || files.length == 0) {
	        return;
	    }

	    String uploadDir = "uploads/";
	    Path uploadPath = Paths.get(uploadDir);

	    if (!Files.exists(uploadPath)) {
	        Files.createDirectories(uploadPath);
	    }

	    for (MultipartFile file : files) {

	        if (file.isEmpty()) {
	            continue;
	        }

	        if (!file.getContentType().startsWith("image/") &&
	            !file.getContentType().startsWith("video/")) {
	            continue;
	        }

	        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
	        Path filePath = uploadPath.resolve(fileName);

	        Files.write(filePath, file.getBytes());

	        TicketMedia media = new TicketMedia();
	        media.setFileName(fileName);
	        media.setFileType(file.getContentType());
	        media.setFilePath(filePath.toString());
	        media.setTicket(ticket);

	        ticketMediaRepository.save(media);
	    }
	}

	}
