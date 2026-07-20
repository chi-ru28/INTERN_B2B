package com.b2becommerce.identityservice.exception;

public class ResetSessionExpiredException extends RuntimeException {
    public ResetSessionExpiredException(String message) {
        super(message);
    }
}
