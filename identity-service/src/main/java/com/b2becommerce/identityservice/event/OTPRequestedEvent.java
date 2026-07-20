package com.b2becommerce.identityservice.event;

import java.time.LocalDateTime;

public record OTPRequestedEvent(
    String email, 
    String eventType, 
    String ipAddress, 
    String browser, 
    String os, 
    String country, 
    LocalDateTime timestamp
) {}
