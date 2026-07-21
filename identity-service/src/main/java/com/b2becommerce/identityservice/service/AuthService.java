package com.b2becommerce.identityservice.service;

import com.b2becommerce.identityservice.dto.LoginRequest;
import com.b2becommerce.identityservice.dto.RegisterRequest;
import com.b2becommerce.identityservice.entity.AccountStatus;
import com.b2becommerce.identityservice.entity.Role;
import com.b2becommerce.identityservice.entity.UserCredential;
import com.b2becommerce.identityservice.repository.RoleRepository;
import com.b2becommerce.identityservice.repository.UserCredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.b2becommerce.identityservice.mail.EmailService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.b2becommerce.identityservice.event.*;
import com.b2becommerce.identityservice.entity.OTPType;
import com.b2becommerce.identityservice.entity.AuditLog;
import com.b2becommerce.identityservice.repository.AuditLogRepository;
import java.time.LocalDateTime;
import java.util.Collections;
@Service
public class AuthService {

    @Autowired
    private UserCredentialRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private com.b2becommerce.identityservice.repository.EmailVerificationTokenRepository emailTokenRepository;

    @Autowired
    private com.b2becommerce.identityservice.repository.PasswordHistoryRepository passwordHistoryRepository;

    @Autowired
    @SuppressWarnings("deprecation")
    private com.b2becommerce.identityservice.repository.PasswordResetTokenRepository resetTokenRepository;

    @Autowired
    private RedisService redisService;

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Transactional
    public java.util.Map<String, Object> register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already taken!");
        }

        UserCredential user = new UserCredential();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        
        Role customerRole = roleRepository.findByName("ROLE_CUSTOMER")
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName("ROLE_CUSTOMER");
                    role.setDescription("Default customer role");
                    return roleRepository.save(role);
                });
                
        user.setRoles(Collections.singleton(customerRole));
        user.setStatus(AccountStatus.PENDING_VERIFICATION);

        user = userRepository.save(user);

        com.b2becommerce.identityservice.entity.PasswordHistory history = 
            new com.b2becommerce.identityservice.entity.PasswordHistory(user, user.getPassword());
        passwordHistoryRepository.save(history);

        String token = java.util.UUID.randomUUID().toString();
        com.b2becommerce.identityservice.entity.EmailVerificationToken emailToken = 
            new com.b2becommerce.identityservice.entity.EmailVerificationToken(token, user);
        emailTokenRepository.save(emailToken);
        
        emailService.sendVerificationEmail(user.getEmail(), user.getName(), token);

        java.util.Map<String, Object> response = new java.util.HashMap<>();
        response.put("message", "Registration successful! Please check your email to verify your account.");
        response.put("_id", user.getId().toString());
        response.put("name", user.getName());
        response.put("email", user.getEmail());
        response.put("role", customerRole.getName());
        return response;
    }

    public java.util.Map<String, Object> login(LoginRequest request) {
        UserCredential user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getAccountLockedUntil() != null && user.getAccountLockedUntil().isAfter(java.time.LocalDateTime.now())) {
            throw new RuntimeException("Account is locked. Try again later.");
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            if (authentication.isAuthenticated()) {
                // Reset failed attempts on success
                if (user.getFailedLoginAttempts() > 0 || user.getAccountLockedUntil() != null) {
                    user.setFailedLoginAttempts(0);
                    user.setAccountLockedUntil(null);
                    userRepository.save(user);
                }

                String accessToken = jwtService.generateToken(request.getEmail());
                String refreshToken = redisService.generateRefreshToken(request.getEmail());
                
                java.util.Map<String, Object> response = new java.util.HashMap<>();
                response.put("success", true);
                response.put("token", accessToken);
                response.put("accessToken", accessToken);
                response.put("refreshToken", refreshToken);

                java.util.Map<String, Object> userObj = new java.util.HashMap<>();
                userObj.put("id", user.getId().toString());
                userObj.put("name", user.getName());
                userObj.put("email", user.getEmail());
                userObj.put("isActive", user.getStatus() == AccountStatus.ACTIVE);
                userObj.put("role", user.getRoles().isEmpty() ? "" : user.getRoles().iterator().next().getName());
                response.put("user", userObj);

                return response;
            }
        } catch (org.springframework.security.core.AuthenticationException e) {
            int attempts = user.getFailedLoginAttempts() + 1;
            user.setFailedLoginAttempts(attempts);
            if (attempts >= 5) {
                user.setAccountLockedUntil(java.time.LocalDateTime.now().plusMinutes(15));
            }
            userRepository.save(user);
            throw new RuntimeException(attempts >= 5 ? "Account locked due to 5 failed attempts." : "Invalid credentials");
        }
        throw new RuntimeException("Invalid access");
    }

    @Transactional
    public String verifyEmail(String token) {
        com.b2becommerce.identityservice.entity.EmailVerificationToken verificationToken = 
            emailTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid verification token"));

        if (verificationToken.getExpiresAt().isBefore(java.time.LocalDateTime.now())) {
            throw new RuntimeException("Verification token has expired");
        }

        UserCredential user = verificationToken.getUser();
        user.setStatus(AccountStatus.ACTIVE);
        userRepository.save(user);
        
        emailTokenRepository.delete(verificationToken);

        return "Email verified successfully! You can now log in.";
    }

    @Transactional
    @Deprecated
    public String forgotPassword(String email) {
        UserCredential user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with this email"));

        String token = java.util.UUID.randomUUID().toString();
        com.b2becommerce.identityservice.entity.PasswordResetToken resetToken = 
            new com.b2becommerce.identityservice.entity.PasswordResetToken(token, user, "0.0.0.0");
        resetTokenRepository.save(resetToken);

        emailService.sendPasswordResetEmail(user.getEmail(), user.getName(), token);

        return "Password reset link sent to your email";
    }

    @Transactional
    @Deprecated
    public String resetPassword(String token, String newPassword) {
        com.b2becommerce.identityservice.entity.PasswordResetToken resetToken = 
            resetTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid reset token"));

        if (resetToken.isUsed() || resetToken.getExpiresAt().isBefore(java.time.LocalDateTime.now())) {
            throw new RuntimeException("Reset token has expired or already been used");
        }

        UserCredential user = resetToken.getUser();
        
        java.util.List<com.b2becommerce.identityservice.entity.PasswordHistory> recentPasswords = 
            passwordHistoryRepository.findTop5ByUserOrderByCreatedAtDesc(user);
        
        for (com.b2becommerce.identityservice.entity.PasswordHistory ph : recentPasswords) {
            if (passwordEncoder.matches(newPassword, ph.getPasswordHash())) {
                throw new RuntimeException("Cannot reuse one of your last 5 passwords");
            }
        }

        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        userRepository.save(user);

        com.b2becommerce.identityservice.entity.PasswordHistory history = 
            new com.b2becommerce.identityservice.entity.PasswordHistory(user, encodedPassword);
        passwordHistoryRepository.save(history);

        resetToken.setUsed(true);
        resetTokenRepository.save(resetToken);

        return "Password reset successful! You can now log in with your new password.";
    }

    // --- New OTP Methods ---

    @Transactional
    public String requestOtp(String email, String ipAddress, String browser, String os, String country) {
        UserCredential user = userRepository.findByEmail(email)
                .orElseThrow(() -> new com.b2becommerce.identityservice.exception.UserNotFoundException("User not found with this email"));

        String otp = redisService.generateAndStoreOtp(OTPType.PASSWORD_RESET, email);

        AuditLog log = new AuditLog(email, "OTP_REQUESTED", ipAddress, browser, os, country, "Requested password reset OTP");
        auditLogRepository.save(log);

        kafkaProducerService.publishEvent("auth-events", new OTPRequestedEvent(email, "OTP_REQUESTED", ipAddress, browser, os, country, LocalDateTime.now()));

        try {
            emailService.sendOtpEmail(user.getEmail(), user.getName(), otp, ipAddress, browser, os, country, LocalDateTime.now().toString());
        } catch (Exception e) {
            // Explicit Rollback of Redis OTP
            redisService.cleanupOtpFlow(email);
            throw e;
        }
        
        return otp;
    }

    @Transactional
    public void verifyOtp(String email, String otpInput) {
        if (!redisService.verifyOtp(OTPType.PASSWORD_RESET, email, otpInput)) {
            throw new com.b2becommerce.identityservice.exception.OtpInvalidException("Invalid or expired OTP");
        }

        redisService.createResetSession(email);
        
        AuditLog log = new AuditLog(email, "OTP_VERIFIED", null, null, null, null, "OTP verified successfully");
        auditLogRepository.save(log);
        
        kafkaProducerService.publishEvent("auth-events", new OTPVerifiedEvent(email, "OTP_VERIFIED", LocalDateTime.now()));
    }

    @Transactional
    public String resendOtp(String email, String ipAddress, String browser, String os, String country) {
        UserCredential user = userRepository.findByEmail(email)
                .orElseThrow(() -> new com.b2becommerce.identityservice.exception.UserNotFoundException("User not found with this email"));

        String newOtp = redisService.resendOtp(OTPType.PASSWORD_RESET, email);
        emailService.sendOtpEmail(user.getEmail(), user.getName(), newOtp, ipAddress, browser, os, country, LocalDateTime.now().toString());
        return newOtp;
    }

    @Transactional
    public void resetPasswordWithSession(String email, String newPassword) {
        if (!redisService.validateResetSession(email)) {
            throw new com.b2becommerce.identityservice.exception.ResetSessionExpiredException("Reset session is invalid or expired. Please verify OTP again.");
        }

        UserCredential user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        java.util.List<com.b2becommerce.identityservice.entity.PasswordHistory> recentPasswords = 
            passwordHistoryRepository.findTop5ByUserOrderByCreatedAtDesc(user);
        
        for (com.b2becommerce.identityservice.entity.PasswordHistory ph : recentPasswords) {
            if (passwordEncoder.matches(newPassword, ph.getPasswordHash())) {
                throw new com.b2becommerce.identityservice.exception.PasswordHistoryViolationException("Cannot reuse one of your last 5 passwords");
            }
        }

        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        userRepository.save(user);

        com.b2becommerce.identityservice.entity.PasswordHistory history = 
            new com.b2becommerce.identityservice.entity.PasswordHistory(user, encodedPassword);
        passwordHistoryRepository.save(history);

        redisService.cleanupOtpFlow(email);

        AuditLog log = new AuditLog(email, "PASSWORD_RESET", null, null, null, null, "Password reset successfully via OTP session");
        auditLogRepository.save(log);

        kafkaProducerService.publishEvent("auth-events", new PasswordResetEvent(email, "PASSWORD_RESET", LocalDateTime.now()));
    }

    @Transactional
    public void updatePassword(String email, String currentPassword, String newPassword) {
        if (currentPassword.equals(newPassword)) {
            throw new RuntimeException("New password cannot be the same as current password.");
        }
        
        UserCredential user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new RuntimeException("Incorrect current password.");
        }

        java.util.List<com.b2becommerce.identityservice.entity.PasswordHistory> recentPasswords = 
            passwordHistoryRepository.findTop5ByUserOrderByCreatedAtDesc(user);
        
        for (com.b2becommerce.identityservice.entity.PasswordHistory ph : recentPasswords) {
            if (passwordEncoder.matches(newPassword, ph.getPasswordHash())) {
                throw new com.b2becommerce.identityservice.exception.PasswordHistoryViolationException("Cannot reuse one of your last 5 passwords");
            }
        }

        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        userRepository.save(user);

        com.b2becommerce.identityservice.entity.PasswordHistory history = 
            new com.b2becommerce.identityservice.entity.PasswordHistory(user, encodedPassword);
        passwordHistoryRepository.save(history);
        
        AuditLog log = new AuditLog(email, "PASSWORD_UPDATED", null, null, null, null, "User updated their password");
        auditLogRepository.save(log);
    }

    public java.util.Map<String, Object> getCurrentUserProfile(String email) {
        if (email == null) {
            throw new RuntimeException("User profile not found.");
        }
        UserCredential user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User profile not found."));

        java.util.Map<String, Object> profile = new java.util.HashMap<>();
        profile.put("id", user.getId().toString());
        profile.put("name", user.getName());
        profile.put("email", user.getEmail());
        profile.put("isActive", user.getStatus() == AccountStatus.ACTIVE);
        profile.put("role", user.getRoles().isEmpty() ? "" : user.getRoles().iterator().next().getName());
        return profile;
    }

    public String refresh(String refreshToken) {
        String email = redisService.getEmailFromRefreshToken(refreshToken);
        if (email == null) {
            throw new RuntimeException("Invalid or expired refresh token");
        }
        
        // Generate new access token
        return jwtService.generateToken(email);
    }

    public void logout(String accessToken, String refreshToken) {
        if (accessToken != null) {
            // Blacklist the access token for 15 minutes (expiration time)
            redisService.blacklistToken(accessToken, 15 * 60);
        }
        if (refreshToken != null) {
            redisService.deleteRefreshToken(refreshToken);
        }
    }
}
