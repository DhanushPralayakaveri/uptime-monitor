package com.proj.uptimemonitor.service;

import com.proj.uptimemonitor.model.MonitoredEndpoint;
import com.proj.uptimemonitor.repository.MonitoredEndpointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PingService {

    @Autowired
    private MonitoredEndpointRepository endpointRepository;

    @Autowired
    private JavaMailSender mailSender;

    // Runs every 30 seconds. In production, this would usually be 60000 (1 minute)
    @Scheduled(fixedRate = 30000)
    @Transactional // Keeps the database session open so we can safely read the User's email
    public void pingEndpoints() {
        List<MonitoredEndpoint> endpoints = endpointRepository.findAll();

        for (MonitoredEndpoint ep : endpoints) {
            long startTime = System.currentTimeMillis();
            boolean isUp = isReachable(ep.getUrl());
            long endTime = System.currentTimeMillis();

            // 1. Calculate Latency
            long responseTime = endTime - startTime;

            // 2. Update Total Checks
            ep.setTotalChecks(ep.getTotalChecks() + 1);

            if (isUp) {
                ep.setSuccessfulChecks(ep.getSuccessfulChecks() + 1);
                ep.setStatus("UP");
                ep.setResponseTimeMs(responseTime);
            } else {
                // 3. Trigger Alert Email if it just went down!
                if ("UP".equals(ep.getStatus()) || "PENDING".equals(ep.getStatus())) {
                    sendDowntimeAlert(ep.getUser().getEmail(), ep.getName(), ep.getUrl());
                }
                ep.setStatus("DOWN");
                ep.setResponseTimeMs(0L); // 0ms because it failed to respond
            }

            // 4. Calculate Uptime SLA Percentage
            double rawPercentage = ((double) ep.getSuccessfulChecks() / ep.getTotalChecks()) * 100;
            ep.setUptimePercentage(Math.round(rawPercentage * 100.0) / 100.0);
            ep.setLastChecked(LocalDateTime.now());

            endpointRepository.save(ep);

            System.out.println("Pinged " + ep.getUrl() + " -> Status: " + ep.getStatus() + " | " + responseTime + "ms | Uptime: " + ep.getUptimePercentage() + "%");
        }
    }

    // The actual network ping logic
    private boolean isReachable(String targetUrl) {
        try {
            URL url = new URL(targetUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(5000); // Give up after 5 seconds
            connection.setReadTimeout(5000);

            int responseCode = connection.getResponseCode();
            return (200 <= responseCode && responseCode <= 399); // Anything in the 200s or 300s is good
        } catch (Exception e) {
            return false;
        }
    }

    // The Email Dispatcher
    private void sendDowntimeAlert(String userEmail, String endpointName, String url) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(userEmail);
            message.setSubject("🚨 CRITICAL ALERT: " + endpointName + " is DOWN");
            message.setText("Uptime Monitor Alert:\n\n" +
                    "Your monitored infrastructure target [" + endpointName + "] has failed to respond to our polling engine.\n" +
                    "Target URL: " + url + "\n" +
                    "Time of failure: " + LocalDateTime.now() + "\n\n" +
                    "Please check your enterprise dashboard to verify system status.");

            mailSender.send(message);
            System.out.println("📧 Downtime alert email dispatched to " + userEmail);
        } catch (Exception e) {
            System.out.println("Failed to send email. Check SMTP settings.");
        }
    }
}