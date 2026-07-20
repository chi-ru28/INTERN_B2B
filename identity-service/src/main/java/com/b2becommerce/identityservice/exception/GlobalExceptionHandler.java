package com.b2becommerce.identityservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OtpExpiredException.class)
    public ResponseEntity<Map<String, Object>> handleOtpExpired(OtpExpiredException ex) {
        return buildErrorResponse(HttpStatus.GONE, "OTP_EXPIRED", ex.getMessage());
    }

    @ExceptionHandler(OtpInvalidException.class)
    public ResponseEntity<Map<String, Object>> handleOtpInvalid(OtpInvalidException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "OTP_INVALID", ex.getMessage());
    }

    @ExceptionHandler(OtpRateLimitExceededException.class)
    public ResponseEntity<Map<String, Object>> handleOtpRateLimit(OtpRateLimitExceededException ex) {
        return buildErrorResponse(HttpStatus.TOO_MANY_REQUESTS, "RATE_LIMIT_EXCEEDED", ex.getMessage());
    }

    @ExceptionHandler(ResetSessionExpiredException.class)
    public ResponseEntity<Map<String, Object>> handleResetSessionExpired(ResetSessionExpiredException ex) {
        return buildErrorResponse(HttpStatus.GONE, "RESET_SESSION_EXPIRED", ex.getMessage());
    }

    @ExceptionHandler(PasswordHistoryViolationException.class)
    public ResponseEntity<Map<String, Object>> handlePasswordHistory(PasswordHistoryViolationException ex) {
        return buildErrorResponse(HttpStatus.CONFLICT, "PASSWORD_HISTORY_VIOLATION", ex.getMessage());
    }

    @ExceptionHandler(AccountLockedException.class)
    public ResponseEntity<Map<String, Object>> handleAccountLocked(AccountLockedException ex) {
        return buildErrorResponse(HttpStatus.FORBIDDEN, "ACCOUNT_LOCKED", ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntime(RuntimeException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "BAD_REQUEST", ex.getMessage());
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(HttpStatus status, String errorCode, String message) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("success", false);
        errorResponse.put("errorCode", errorCode);
        errorResponse.put("message", message);
        errorResponse.put("timestamp", Instant.now().toString());
        return new ResponseEntity<>(errorResponse, status);
    }
}
