package com.b2becommerce.identityservice.event;

import java.time.LocalDateTime;

public record OTPVerifiedEvent(
    String email, 
    String eventType, 
    LocalDateTime timestamp
) {}
