package com.proj.uptimemonitor.service;

import com.proj.uptimemonitor.model.MonitoredEndpoint;
import com.proj.uptimemonitor.repository.MonitoredEndpointRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PollingService {

    private final MonitoredEndpointRepository endpointRepository;

    public PollingService(MonitoredEndpointRepository endpointRepository) {
        this.endpointRepository = endpointRepository;
    }

    // This tells Spring Boot to run this method automatically every 60 seconds
    @Scheduled(fixedRate = 30000)
    public void pingEndpoints() {
        System.out.println("Starting polling cycle at " + LocalDateTime.now());

        // Grab all URLs from your Neon database
        List<MonitoredEndpoint> endpoints = endpointRepository.findAll();

        if (endpoints.isEmpty()) {
            System.out.println("No endpoints found. Add some URLs via Postman!");
            return;
        }

        // Loop through each URL and ping it
        for (MonitoredEndpoint endpoint : endpoints) {
            String targetUrl = endpoint.getUrl();
            boolean isOnline = checkWebsite(targetUrl);

            // Update the status
            endpoint.setStatus(isOnline ? "UP" : "DOWN");
            endpoint.setLastChecked(LocalDateTime.now());

            // Save the updated status back to Neon
            endpointRepository.save(endpoint);

            System.out.println("Pinged " + targetUrl + " -> Status: " + endpoint.getStatus());
        }
    }

    // Helper method to send a simple GET request
    private boolean checkWebsite(String targetUrl) {
        try {
            URL url = new URL(targetUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.connect();

            int code = connection.getResponseCode();
            return code >= 200 && code < 400; // 2xx and 3xx codes mean success
        } catch (Exception e) {
            return false; // If it times out or throws an error, it is down
        }
    }
}