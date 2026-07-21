package com.b2becommerce.identityservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ControllerAdvice
@SuppressWarnings("null")
public class GlobalExceptionHandler {

    @ExceptionHandler(OtpExpiredException.class)
    public ResponseEntity<Map<String, Object>> handleOtpExpired(OtpExpiredException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.GONE, "OTP_EXPIRED", ex.getMessage(), "Your verification code has expired.", request);
    }

    @ExceptionHandler(OtpInvalidException.class)
    public ResponseEntity<Map<String, Object>> handleOtpInvalid(OtpInvalidException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "OTP_INVALID", ex.getMessage(), "Incorrect verification code.", request);
    }

    @ExceptionHandler(OtpRateLimitExceededException.class)
    public ResponseEntity<Map<String, Object>> handleOtpRateLimit(OtpRateLimitExceededException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.TOO_MANY_REQUESTS, "OTP_RATE_LIMIT", "Too many requests. Please try again later.", ex.getMessage(), request);
    }

    @ExceptionHandler(ResetSessionExpiredException.class)
    public ResponseEntity<Map<String, Object>> handleResetSessionExpired(ResetSessionExpiredException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.GONE, "RESET_SESSION_EXPIRED", ex.getMessage(), "Your reset session is invalid or expired.", request);
    }

    @ExceptionHandler(PasswordHistoryViolationException.class)
    public ResponseEntity<Map<String, Object>> handlePasswordHistory(PasswordHistoryViolationException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.CONFLICT, "PASSWORD_HISTORY_VIOLATION", ex.getMessage(), "Please use a different password.", request);
    }

    @ExceptionHandler(AccountLockedException.class)
    public ResponseEntity<Map<String, Object>> handleAccountLocked(AccountLockedException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.FORBIDDEN, "ACCOUNT_LOCKED", ex.getMessage(), "Your account has been locked.", request);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotFound(UserNotFoundException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "No account found with this email.", ex.getMessage(), request);
    }

    @ExceptionHandler(SmtpAuthException.class)
    public ResponseEntity<Map<String, Object>> handleSmtpAuthException(SmtpAuthException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.SERVICE_UNAVAILABLE, "SMTP_AUTH_FAILED", "Email service is temporarily unavailable.", ex.getMessage(), request);
    }

    @ExceptionHandler(SmtpConfigurationMissingException.class)
    public ResponseEntity<Map<String, Object>> handleSmtpConfigurationMissingException(SmtpConfigurationMissingException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "SMTP_CONFIGURATION_MISSING", "SMTP credentials are not configured.", ex.getMessage(), request);
    }

    @ExceptionHandler(RedisUnavailableException.class)
    public ResponseEntity<Map<String, Object>> handleRedisUnavailableException(RedisUnavailableException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.SERVICE_UNAVAILABLE, "REDIS_UNAVAILABLE", "Verification service is temporarily unavailable.", ex.getMessage(), request);
    }

    @ExceptionHandler(org.springframework.data.redis.RedisSystemException.class)
    public ResponseEntity<Map<String, Object>> handleRedisSystemException(org.springframework.data.redis.RedisSystemException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.SERVICE_UNAVAILABLE, "REDIS_UNAVAILABLE", "Verification service is temporarily unavailable.", ex.getMessage(), request);
    }

    @ExceptionHandler(OtpCooldownException.class)
    public ResponseEntity<Map<String, Object>> handleOtpCooldown(OtpCooldownException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.TOO_MANY_REQUESTS, "OTP_COOLDOWN", "Please wait 60 seconds before requesting another code.", ex.getMessage(), request);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntime(RuntimeException ex, HttpServletRequest request) {
        String message = ex.getMessage();
        if (message == null || message.trim().isEmpty()) {
            message = "An internal server error occurred.";
        }
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "BAD_REQUEST", message, "An unexpected error occurred.", request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception ex, HttpServletRequest request) {
        String message = ex.getMessage();
        if (message == null || message.trim().isEmpty()) {
            message = "An unexpected error occurred.";
        }
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", message, "An internal server error occurred.", request);
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(HttpStatus status, String errorCode, String message, String details, HttpServletRequest request) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("success", false);
        errorResponse.put("errorCode", errorCode);
        errorResponse.put("message", message);
        errorResponse.put("details", details);
        errorResponse.put("timestamp", Instant.now().toString());
        errorResponse.put("path", request.getRequestURI());
        errorResponse.put("traceId", UUID.randomUUID().toString());
        return new ResponseEntity<>(errorResponse, status);
    }
}
