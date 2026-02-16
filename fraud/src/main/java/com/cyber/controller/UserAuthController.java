package com.cyber.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.customer.exception.AlreadyEmailExisted;
import com.customer.exception.InvalidCredentialsException;
import com.customer.exception.UserNotFoundException;
import com.cyber.CrimeReportSystemApplication;
import com.cyber.config.EmailConfiguration;
import com.cyber.model.Ticket;
import com.cyber.model.TicketStatus;
import com.cyber.model.User;
import com.cyber.service.TicketService;
import com.cyber.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
public class UserAuthController
{

    
   @Autowired
   private UserService userService;
   
   @Autowired
   private EmailConfiguration emailConfiguration;
   
   @Autowired
   private TicketService ticketService;
    
   
   @PostMapping("/register")
   public String userRegistration(@ModelAttribute User users,Model model,RedirectAttributes redirectAtrribute)
   {
       try
       {
           userService.register(users);
           emailConfiguration.userRegistrationMailFormat(users.getEmail(), users.getFullName());
           return "redirect:/login";
       } 
       catch (AlreadyEmailExisted e) 
       {
    	   //model.addAttribute("error",e.getMessage());
    	   redirectAtrribute.addFlashAttribute("error",e.getMessage());
           return "redirect:/register";
       }
   }

   @PostMapping("/login")
   public String userLogin(@RequestParam String email,
                           @RequestParam String password,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {

       try {
           User user = userService.login(email, password);

           session.setAttribute("loggedUserId", user.getId());
           session.setAttribute("loggedUserName", user.getFullName());
           session.setAttribute("role", "USER");

           return "redirect:/dashboard";

       } catch (UserNotFoundException | InvalidCredentialsException ex) {

           //model.addAttribute("error", ex.getMessage());
           redirectAttributes.addFlashAttribute("error", ex.getMessage());
           return "redirect:/login";   
       }
   }



   
   @GetMapping("/mytickets")
   public String myTickets(
           @RequestParam(required = false) Long id,
           Model model,
           HttpSession session) {

       Long userId = (Long) session.getAttribute("loggedUserId");
       if (userId == null) {
           return "redirect:/login";
       }

       // 1. Fetch all tickets for stats
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

       model.addAttribute("totalTickets", total);
       model.addAttribute("openTickets", open);
       model.addAttribute("progressTickets", progress);
       model.addAttribute("closedTickets", closed);

       // 2. Table: empty by default
       List<Ticket> tickets = List.of();

       // If user searched by ID
       if (id != null) {
           Ticket ticket = ticketService.getTicketByIdAndUserId(id, userId);
           if (ticket != null) {
               tickets = List.of(ticket);
           } else {
               model.addAttribute("message", "No ticket found with that ID");
           }
       }

       model.addAttribute("tickets", tickets);

       return "mytickets";
   }

}
