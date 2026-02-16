package com.cyber.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.cyber.config.EmailConfiguration;
import com.cyber.config.PasswordConfig;
import com.cyber.model.Ticket;
import com.cyber.model.TicketStatus;
import com.cyber.model.User;
import com.cyber.repositories.UserRepository;
import com.cyber.service.TicketService;

import jakarta.servlet.http.HttpSession;

@Controller
public class PageController {

    @Autowired
    private TicketService ticketService;
    
    @Autowired
    private EmailConfiguration emailConfiguration;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;



    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage()
    {
        return "register";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {

        Long userId = (Long) session.getAttribute("loggedUserId");
        if (userId == null) {
            return "redirect:/login";
        }

        

        return "dashboard";
    }


    @PostMapping("/ticket/create/report")
    public String createTicket(
            Ticket ticket,
            @RequestParam(value = "files", required = false) MultipartFile[] files,
            HttpSession session) throws IOException {

        Long userId = (Long) session.getAttribute("loggedUserId");

        if (userId == null) {
            return "redirect:/login";
        }

        // Save ticket
        Ticket savedTicket = ticketService.createTicket(userId, ticket);

        // Save media if uploaded
        if (files != null && files.length > 0) {
            ticketService.saveTicketMedia(savedTicket, files);
        }

        // Get user details
        String userName = savedTicket.getUser().getFullName();
        String userEmail = savedTicket.getUser().getEmail();

        // Send mail
        emailConfiguration.ticketGeneratedMailWithId(
                userEmail,
                userName,
                savedTicket.getId(),
                savedTicket.getIncidentType()
        );

        return "redirect:/mytickets";
    }


    
    @GetMapping("/report")
    public String reportTicket(HttpSession session) {

        if (session.getAttribute("loggedUserId") == null) {
            return "redirect:/login";
        }

        return "report";
    }


    @GetMapping("/mytickets")
    public String myTickets(
            @RequestParam(required = false) Long ticketId,
            Model model,
            HttpSession session) {

        Long userId = (Long) session.getAttribute("loggedUserId");

        if (userId == null) {
            return "redirect:/login";
        }

        List<Ticket> tickets = List.of();

        boolean searched = false;

        if (ticketId != null) {

            searched = true;

            Ticket ticket = ticketService.getTicketById(ticketId);

            if (ticket != null && ticket.getUser().getId().equals(userId)) {
                tickets = List.of(ticket);
            }
        }

        // Stats always from all tickets
        List<Ticket> allTickets = ticketService.getTicketsByUserId(userId);

        long total = allTickets.size();
        long open = allTickets.stream()
                .filter(t -> t.getStatus() == TicketStatus.REPORTED)
                .count();

        long progress = allTickets.stream()
                .filter(t -> t.getStatus() == TicketStatus.UNDER_REVIEW)
                .count();

        long closed = allTickets.stream()
                .filter(t -> t.getStatus() == TicketStatus.RESOLVED)
                .count();

        model.addAttribute("tickets", tickets);
        model.addAttribute("searched", searched);

        model.addAttribute("totalTickets", total);
        model.addAttribute("openTickets", open);
        model.addAttribute("progressTickets", progress);
        model.addAttribute("closedTickets", closed);

        return "mytickets";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
    
    @GetMapping("/awareness")
    public String awareness() {
        return "awareness";
    }
    
    @GetMapping("/newsarticles")
    public String newsarticles() {
        return "newsarticles";
    }
    
    @GetMapping("/forgot")
    public String forgot() {
        return "forgot";
    }
    
    @PostMapping("/reset_password")
    public String resetPassword( @RequestParam String email, @RequestParam String newPassword,
    		@RequestParam String confirmPassword,
          Model model
    ) 
    {

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            return "forgot";
        }

        Optional<User> optionalUser = userRepository.findByEmail(email);


        if (optionalUser.isEmpty()) {
            model.addAttribute("error", "Email not registered");
            return "forgot";
        }
         
        User user = optionalUser.get();

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return "redirect:/login";
    }

}
