package com.proj.uptimemonitor.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "monitored_endpoints")
public class MonitoredEndpoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String url;

    private String status;

    private LocalDateTime lastChecked;

    // The core connection back to the User who owns this endpoint
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore // Prevents infinite recursion when sending JSON back to Angular
    private User user;

    // --- NEW TELEMETRY FIELDS ---
    private Long responseTimeMs = 0L;
    private Integer totalChecks = 0;
    private Integer successfulChecks = 0;
    private Double uptimePercentage = 100.0;

    // ==========================================
    // GETTERS AND SETTERS (Completely written out)
    // ==========================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getLastChecked() {
        return lastChecked;
    }

    public void setLastChecked(LocalDateTime lastChecked) {
        this.lastChecked = lastChecked;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getResponseTimeMs() {
        return responseTimeMs;
    }

    public void setResponseTimeMs(Long responseTimeMs) {
        this.responseTimeMs = responseTimeMs;
    }

    public Integer getTotalChecks() {
        return totalChecks;
    }

    public void setTotalChecks(Integer totalChecks) {
        this.totalChecks = totalChecks;
    }

    public Integer getSuccessfulChecks() {
        return successfulChecks;
    }

    public void setSuccessfulChecks(Integer successfulChecks) {
        this.successfulChecks = successfulChecks;
    }

    public Double getUptimePercentage() {
        return uptimePercentage;
    }

    public void setUptimePercentage(Double uptimePercentage) {
        this.uptimePercentage = uptimePercentage;
    }
}