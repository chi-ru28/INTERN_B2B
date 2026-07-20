package com.b2becommerce.identityservice.controller;

import com.b2becommerce.identityservice.entity.UserCredential;
import com.b2becommerce.identityservice.service.AuthService;
import com.b2becommerce.identityservice.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.HashMap;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import jakarta.mail.internet.MimeMessage;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private JavaMailSender mailSender;

    @PostMapping("/register")
    public java.util.Map<String, String> register(@jakarta.validation.Valid @RequestBody com.b2becommerce.identityservice.dto.RegisterRequest request) {
        String message = authService.register(request);
        java.util.Map<String, String> response = new java.util.HashMap<>();
        response.put("message", message);
        return response;
    }

    @PostMapping("/login")
    public java.util.Map<String, String> login(@jakarta.validation.Valid @RequestBody com.b2becommerce.identityservice.dto.LoginRequest request) {
        return authService.login(request);
    }

    @GetMapping("/validate")
    public String validateToken(@RequestParam("token") String token) {
        jwtService.validateToken(token);
        return "Token is valid";
    }

    @PostMapping("/forgot-password")
    @Deprecated
    public java.util.Map<String, String> forgotPassword(@RequestBody java.util.Map<String, String> payload) {
        String email = payload.get("email");
        if (email == null || email.isEmpty()) {
            throw new RuntimeException("Email is required");
        }
        
        String message = authService.forgotPassword(email);

        java.util.Map<String, String> response = new java.util.HashMap<>();
        response.put("message", message);
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

    @PostMapping("/request-otp")
    public Map<String, Object> requestOtp(@RequestBody Map<String, String> payload, HttpServletRequest request) {
        String email = payload.get("email");
        if (email == null || email.isEmpty()) {
            throw new RuntimeException("Email is required");
        }

        String ipAddress = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        String browser = userAgent != null ? userAgent : "Unknown";
        String os = userAgent != null ? userAgent : "Unknown";
        String country = "Unknown"; // Mocked for now

        authService.requestOtp(email, ipAddress, browser, os, country);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "OTP sent to your email");
        return response;
    }

    @PostMapping("/verify-otp")
    public Map<String, Object> verifyOtp(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String otp = payload.get("otp");
        if (email == null || email.isEmpty() || otp == null || otp.isEmpty()) {
            throw new RuntimeException("Email and OTP are required");
        }

        authService.verifyOtp(email, otp);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "OTP verified successfully");
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

        authService.resendOtp(email, ipAddress, browser, os, country);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "OTP resent to your email");
        return response;
    }

    @PostMapping("/reset-password")
    public Map<String, Object> resetPassword(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String newPassword = payload.get("newPassword");

        if (email == null || email.isEmpty() || newPassword == null || newPassword.isEmpty()) {
            throw new RuntimeException("Email and newPassword are required");
        }

        authService.resetPasswordWithSession(email, newPassword);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Password reset successful");
        return response;
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
