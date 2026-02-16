package com.cyber.service;

import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.customer.exception.AlreadyEmailExisted;
import com.customer.exception.InvalidCredentialsException;
import com.customer.exception.UserNotFoundException;
import com.cyber.model.User;

import com.cyber.repositories.UserRepository;

@Service
public class UserService 
{
   @Autowired
   private UserRepository userRepository;
   
   @Autowired
   private PasswordEncoder passwordEncoder;

   
   public User register(User user) 
   {
	    Optional<User> existing = userRepository.findByEmail(user.getEmail());
	    if (existing.isPresent()) {
	        throw new AlreadyEmailExisted("Email already registered");
	    }
	    
	 // encode password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
	    return userRepository.save(user);
	}
   
   // user Login
   public User login(String email, String password)
   {
      User user = userRepository.findByEmail(email)
              .orElseThrow(() -> new UserNotFoundException("User not found"));

      if (!passwordEncoder.matches(password, user.getPassword())) {
          throw new InvalidCredentialsException("Invalid password");
      }

      return user;
   }


}
