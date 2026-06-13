package com.proj.uptimemonitor.controller;

import com.proj.uptimemonitor.model.User;
import com.proj.uptimemonitor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        // 1. Check if email is already taken
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Error: Email is already in use!");
        }

        // 2. Hash the plain text password before it touches Neon
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 3. Save the new user
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok("User registered successfully with ID: " + savedUser.getId());
    }
}