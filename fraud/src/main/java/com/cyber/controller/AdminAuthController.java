package com.cyber.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.customer.exception.AdminInvalidCredentials;
import com.customer.exception.AdminNotFoundException;
import com.cyber.model.TicketStatus;
import com.cyber.model.User;
import com.cyber.repositories.UserRepository;
import com.cyber.service.AdminService;

import org.springframework.ui.Model;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminAuthController {

    @Autowired
    private AdminService adminService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {

        if (!"ADMIN".equals(session.getAttribute("role"))) {
            return "redirect:/admin/login";
        }

        model.addAttribute("tickets", adminService.getAllTickets());
        model.addAttribute("statuses", TicketStatus.values());
        return "admindashboard";
    }

    @PostMapping("/ticket/status")
    public String updateStatus(@RequestParam Long ticketId, @RequestParam TicketStatus status) {

        adminService.updateTicketStatus(ticketId, status);
        return "redirect:/admin/dashboard?updated=true";
    }
    
    @PostMapping("/login")
    public String adminLogin(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,Model model,RedirectAttributes redirectAttributes) {

        try {
            User admin = adminService.login(email, password);

            session.setAttribute("loggedAdminId", admin.getId());
            session.setAttribute("loggedAdminEmail", admin.getEmail());
            session.setAttribute("role", "ADMIN");

            return "redirect:/admin/dashboard";
        }
        catch (AdminNotFoundException | AdminInvalidCredentials e) {
        	
        	redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/login";
        }
    }
    
    @PostMapping("/reset_password")
    public String resetPassword(
            @RequestParam String email,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            Model model
    ) {

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            return "adminforgot";
        }

        Optional<User> optionalUser = userRepository.findByEmail(email);


        if (optionalUser.isEmpty()) {
            model.addAttribute("error", "Email not registered");
            return "adminforgot";
        }
         
        User user = optionalUser.get();

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return "redirect:/admin/login";
    }
    

    
}
