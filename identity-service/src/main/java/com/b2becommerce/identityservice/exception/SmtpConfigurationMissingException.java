package com.b2becommerce.identityservice.exception;

public class SmtpConfigurationMissingException extends RuntimeException {
    public SmtpConfigurationMissingException(String message) {
        super(message);
    }
}
