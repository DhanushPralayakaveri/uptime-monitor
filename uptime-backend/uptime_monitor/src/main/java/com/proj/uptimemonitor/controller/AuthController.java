package com.proj.uptimemonitor.controller;

import com.proj.uptimemonitor.dto.AuthRequest;
import com.proj.uptimemonitor.dto.AuthResponse;
import com.proj.uptimemonitor.model.User;
import com.proj.uptimemonitor.repository.UserRepository;
import com.proj.uptimemonitor.security.CustomUserDetailsService;
import com.proj.uptimemonitor.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthRequest authRequest) {
        try {
            // 1. Spring Security checks the email and password
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            // 2. If it fails, send a 401 Unauthorized back to Angular
            return ResponseEntity.status(401).body("Incorrect email or password");
        }

        // 3. If successful, load the user and generate the JWT
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails);

        // 4. Get the user ID so Angular knows which dashboard to load
        User user = userRepository.findByEmail(authRequest.getEmail()).get();

        // 5. Send the token back!
        return ResponseEntity.ok(new AuthResponse(jwt, user.getId()));
    }
}