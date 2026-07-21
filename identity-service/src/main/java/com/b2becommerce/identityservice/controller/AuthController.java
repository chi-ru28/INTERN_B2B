package com.b2becommerce.identityservice.controller;

import com.b2becommerce.identityservice.service.AuthService;
import com.b2becommerce.identityservice.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public java.util.Map<String, Object> register(@jakarta.validation.Valid @RequestBody com.b2becommerce.identityservice.dto.RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public java.util.Map<String, Object> login(@jakarta.validation.Valid @RequestBody com.b2becommerce.identityservice.dto.LoginRequest request) {
        return authService.login(request);
    }

    @GetMapping("/validate")
    public String validateToken(@RequestParam("token") String token) {
        jwtService.validateToken(token);
        return "Token is valid";
    }

    @PostMapping("/request-otp")
    public java.util.Map<String, Object> requestOtp(@RequestBody java.util.Map<String, String> payload, HttpServletRequest request) {
        String email = payload.get("email");
        if (email == null || email.isEmpty()) {
            throw new RuntimeException("Email is required");
        }

        String ipAddress = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        String browser = userAgent != null ? userAgent : "Unknown";
        String os = userAgent != null ? userAgent : "Unknown";
        String country = "Unknown";

        String otp = authService.requestOtp(email, ipAddress, browser, os, country);

        java.util.Map<String, Object> response = new java.util.HashMap<>();
        response.put("message", "A one-time password has been sent to your email.");
        response.put("devOtp", otp);
        return response;
    }

    @PostMapping("/verify-email")
    public java.util.Map<String, String> verifyEmail(@RequestBody java.util.Map<String, String> payload) {
        String token = payload.get("token");
        if (token == null || token.isEmpty()) {
            throw new RuntimeException("Token is required");
        }

        String message = authService.verifyEmail(token);

        java.util.Map<String, String> response = new java.util.HashMap<>();
        response.put("message", message);
        return response;
    }

    @PostMapping("/reset-password-old")
    @Deprecated
    public java.util.Map<String, String> resetPasswordOld(@RequestBody java.util.Map<String, String> payload) {
        String token = payload.get("token");
        String newPassword = payload.get("newPassword");
        
        if (token == null || token.isEmpty() || newPassword == null || newPassword.isEmpty()) {
            throw new RuntimeException("Token and newPassword are required");
        }

        String message = authService.resetPassword(token, newPassword);

        java.util.Map<String, String> response = new java.util.HashMap<>();
        response.put("message", message);
        return response;
    }

    // --- New OTP Endpoints ---

    // Request OTP is now handled by /forgot-password

    @PostMapping("/verify-otp")
    public java.util.Map<String, Object> verifyOtp(@RequestBody java.util.Map<String, String> payload) {
        String email = payload.get("email");
        String otp = payload.get("otp");
        if (email == null || email.isEmpty() || otp == null || otp.isEmpty()) {
            throw new RuntimeException("Email and OTP are required");
        }

        authService.verifyOtp(email, otp);

        java.util.Map<String, Object> response = new java.util.HashMap<>();
        response.put("success", true);
        response.put("message", "OTP verified successfully.");
        return response;
    }

    @PostMapping("/resend-otp")
    public Map<String, Object> resendOtp(@RequestBody Map<String, String> payload, HttpServletRequest request) {
        String email = payload.get("email");
        if (email == null || email.isEmpty()) {
            throw new RuntimeException("Email is required");
        }

        String ipAddress = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        String browser = userAgent != null ? userAgent : "Unknown";
        String os = userAgent != null ? userAgent : "Unknown";
        String country = "Unknown";

        String newOtp = authService.resendOtp(email, ipAddress, browser, os, country);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "OTP resent to your email");
        response.put("devOtp", newOtp);
        return response;
    }

    @PostMapping("/reset-password")
    public java.util.Map<String, Object> resetPassword(@RequestBody java.util.Map<String, String> payload) {
        String email = payload.get("email");
        String newPassword = payload.get("newPassword");

        if (email == null || email.isEmpty() || newPassword == null || newPassword.isEmpty()) {
            throw new RuntimeException("Email and newPassword are required");
        }

        authService.resetPasswordWithSession(email, newPassword);

        java.util.Map<String, Object> response = new java.util.HashMap<>();
        response.put("message", "Password updated successfully. You can sign in with your new password.");
        return response;
    }

    @PostMapping("/update-password")
    public java.util.Map<String, Object> updatePassword(@RequestBody java.util.Map<String, String> payload, @RequestHeader("loggedInUser") String email) {
        String currentPassword = payload.get("currentPassword");
        String newPassword = payload.get("newPassword");

        if (currentPassword == null || newPassword == null) {
            throw new RuntimeException("Please provide both current and new passwords.");
        }

        authService.updatePassword(email, currentPassword, newPassword);

        java.util.Map<String, Object> response = new java.util.HashMap<>();
        response.put("success", true);
        response.put("message", "Password updated successfully.");
        return response;
    }

    @GetMapping("/me")
    public java.util.Map<String, Object> getCurrentUserProfile(@RequestHeader("loggedInUser") String email) {
        return authService.getCurrentUserProfile(email);
    }

    @PostMapping("/refresh")
    public java.util.Map<String, String> refresh(@RequestBody java.util.Map<String, String> payload) {
        String refreshToken = payload.get("refreshToken");
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new RuntimeException("Refresh token is required");
        }

        String newAccessToken = authService.refresh(refreshToken);
        java.util.Map<String, String> response = new java.util.HashMap<>();
        response.put("accessToken", newAccessToken);
        return response;
    }

    @PostMapping("/logout")
    public java.util.Map<String, String> logout(@RequestHeader("Authorization") String authHeader,
                                                @RequestBody java.util.Map<String, String> payload) {
        String refreshToken = payload.get("refreshToken");
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            authService.logout(token, refreshToken);
        }

        java.util.Map<String, String> response = new java.util.HashMap<>();
        response.put("message", "Logged out successfully");
        return response;
    }
}
