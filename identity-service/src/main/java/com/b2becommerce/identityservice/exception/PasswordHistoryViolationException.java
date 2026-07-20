package com.b2becommerce.identityservice.exception;

public class PasswordHistoryViolationException extends RuntimeException {
    public PasswordHistoryViolationException(String message) {
        super(message);
    }
}
