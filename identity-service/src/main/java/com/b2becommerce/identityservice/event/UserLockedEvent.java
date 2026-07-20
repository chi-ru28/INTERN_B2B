package com.b2becommerce.identityservice.event;

import java.time.LocalDateTime;

public record UserLockedEvent(
    String email, 
    String eventType, 
    LocalDateTime timestamp
) {}
