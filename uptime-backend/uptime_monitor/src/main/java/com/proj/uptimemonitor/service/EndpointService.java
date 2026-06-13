package com.proj.uptimemonitor.service;

import com.proj.uptimemonitor.model.MonitoredEndpoint;
import com.proj.uptimemonitor.model.User;
import com.proj.uptimemonitor.repository.MonitoredEndpointRepository;
import com.proj.uptimemonitor.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EndpointService {

    private final MonitoredEndpointRepository endpointRepository;
    private final UserRepository userRepository;

    public EndpointService(MonitoredEndpointRepository endpointRepository, UserRepository userRepository) {
        this.endpointRepository = endpointRepository;
        this.userRepository = userRepository;
    }

    public MonitoredEndpoint addEndpoint(MonitoredEndpoint endpoint, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        endpoint.setUser(user);
        endpoint.setStatus("PENDING");
        return endpointRepository.save(endpoint);
    }

    public List<MonitoredEndpoint> getEndpointsByUserId(Long userId) {
        return endpointRepository.findByUserId(userId);
    }

    public void deleteEndpoint(Long endpointId) {
        endpointRepository.deleteById(endpointId);
    }
}