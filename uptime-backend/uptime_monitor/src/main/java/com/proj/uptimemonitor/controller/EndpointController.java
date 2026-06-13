package com.proj.uptimemonitor.controller;

import com.proj.uptimemonitor.dto.EndpointRequest;
import com.proj.uptimemonitor.model.MonitoredEndpoint;
import com.proj.uptimemonitor.model.User;
import com.proj.uptimemonitor.repository.MonitoredEndpointRepository;
import com.proj.uptimemonitor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/endpoints")
public class EndpointController {

    @Autowired
    private MonitoredEndpointRepository endpointRepository;

    @Autowired
    private UserRepository userRepository;

    // 1. GET endpoints securely using the JWT Bouncer's identity
    @GetMapping
    public ResponseEntity<List<MonitoredEndpoint>> getMyEndpoints(Principal principal) {
        User user = userRepository.findByEmail(principal.getName()).orElseThrow();
        return ResponseEntity.ok(endpointRepository.findByUserId(user.getId()));
    }

    // 2. POST endpoints securely using the JWT Bouncer's identity
    @PostMapping
    public ResponseEntity<?> addEndpoint(@RequestBody EndpointRequest request, Principal principal) {
        User user = userRepository.findByEmail(principal.getName()).orElseThrow();

        MonitoredEndpoint endpoint = new MonitoredEndpoint();
        endpoint.setName(request.getName());
        endpoint.setUrl(request.getUrl());
        endpoint.setStatus("PENDING");
        endpoint.setUser(user);

        return ResponseEntity.ok(endpointRepository.save(endpoint));
    }
    // 3. DELETE an endpoint securely
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEndpoint(@PathVariable Long id, Principal principal) {
        User user = userRepository.findByEmail(principal.getName()).orElseThrow();
        Optional<MonitoredEndpoint> endpoint = endpointRepository.findById(id);

        // Security check: Make sure the endpoint exists AND belongs to the logged-in user
        if (endpoint.isPresent() && endpoint.get().getUser().getId().equals(user.getId())) {
            endpointRepository.delete(endpoint.get());
            return ResponseEntity.ok().body("{\"message\": \"Monitor decommissioned\"}");
        }

        return ResponseEntity.status(403).body("{\"error\": \"Unauthorized or not found\"}");
    }
}