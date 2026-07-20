package com.b2becommerce.identityservice.exception;

public class OtpRateLimitExceededException extends OtpException {
    public OtpRateLimitExceededException(String message) {
        super(message);
    }
}
