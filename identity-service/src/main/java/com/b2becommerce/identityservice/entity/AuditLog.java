package com.b2becommerce.identityservice.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String eventType;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    private String ipAddress;
    private String browser;
    private String os;
    private String country;
    
    @Column(columnDefinition = "TEXT")
    private String details;

    public AuditLog() {
        this.timestamp = LocalDateTime.now();
    }

    public AuditLog(String email, String eventType, String ipAddress, String browser, String os, String country, String details) {
        this.email = email;
        this.eventType = eventType;
        this.ipAddress = ipAddress;
        this.browser = browser;
        this.os = os;
        this.country = country;
        this.details = details;
        this.timestamp = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    public String getBrowser() { return browser; }
    public void setBrowser(String browser) { this.browser = browser; }
    public String getOs() { return os; }
    public void setOs(String os) { this.os = os; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
}
