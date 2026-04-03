package com.ved.User.service.controller;

import com.ved.User.service.Entity.User;
import com.ved.User.service.Repository.UserRepository;
import com.ved.User.service.service.JwtService; // Make sure this import is correct
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService; // FIX: Added missing injection

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "User Registered Successfully";
    }

    @GetMapping("/validate/{id}")
    public User getUser(@PathVariable Integer id) {
        return userRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        // 1. Find the user in DB using getUsername()
        User existingUser = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Verify password
        if (passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            // 3. Generate Token using getUsername() to match your Entity
            return jwtService.generateToken(user.getUsername());
        } else {
            return "Invalid Credentials";
        }
    }
}