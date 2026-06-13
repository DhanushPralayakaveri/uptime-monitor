package com.proj.uptimemonitor.dto;

public class EndpointRequest {
    private String name;
    private String url;
    private Long userId;

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}