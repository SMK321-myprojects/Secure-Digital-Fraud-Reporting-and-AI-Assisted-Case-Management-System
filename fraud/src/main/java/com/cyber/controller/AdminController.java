package com.cyber.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cyber.model.User;
import com.cyber.repositories.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController
{
	

    @GetMapping("/login")
    public String adminLoginPage() {
        return "adminLogin";
    }

    
    @GetMapping("/adminforgot")
    public String adminforgot() {
    	return "adminforgot";
    }
    
    @GetMapping("/logout")
    public String adminLogout()
    {
    	return "adminlogin";
    }

}

