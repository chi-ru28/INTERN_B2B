package com.b2becommerce.identityservice.service;

import com.b2becommerce.identityservice.config.OtpConfigProperties;
import com.b2becommerce.identityservice.entity.OTPType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;
import java.util.UUID;
import java.security.SecureRandom;
import java.util.Map;
import java.util.HashMap;

@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private OtpConfigProperties otpConfig;

    private static final String BLACKLIST_PREFIX = "jwt:blacklist:";
    private static final String REFRESH_TOKEN_PREFIX = "jwt:refresh:";

    public void blacklistToken(String token, long expirationInSeconds) {
        redisTemplate.opsForValue().set(BLACKLIST_PREFIX + token, "invalidated", expirationInSeconds, TimeUnit.SECONDS);
    }

    public boolean isTokenBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(BLACKLIST_PREFIX + token));
    }

    public String generateRefreshToken(String email) {
        String refreshToken = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(REFRESH_TOKEN_PREFIX + refreshToken, email, 7, TimeUnit.DAYS);
        return refreshToken;
    }

    public String getEmailFromRefreshToken(String refreshToken) {
        return (String) redisTemplate.opsForValue().get(REFRESH_TOKEN_PREFIX + refreshToken);
    }

    public void deleteRefreshToken(String refreshToken) {
        redisTemplate.delete(REFRESH_TOKEN_PREFIX + refreshToken);
    }

    // --- OTP Methods ---

    private String getOtpKey(OTPType type, String email) {
        return "otp:" + type.name().toLowerCase() + ":" + email;
    }

    private String getRateLimitKey(String email, String interval) {
        return "rate-limit:otp:" + interval + ":" + email;
    }

    private String getCooldownKey(String email) {
        return "otp-cooldown:" + email;
    }

    private String getFailedAttemptKey(String email) {
        return "failed-attempt:otp:" + email;
    }

    private String getResetSessionKey(String email) {
        return "reset-session:" + email;
    }

    public String generateAndStoreOtp(OTPType type, String email) {
        checkRateLimits(email);
        checkCooldown(email);

        String otp = generateNumericOtp(otpConfig.getLength());
        String hashedOtp = passwordEncoder.encode(otp);

        String key = getOtpKey(type, email);
        Map<String, Object> otpData = new HashMap<>();
        otpData.put("hash", hashedOtp);
        otpData.put("resends", 0);

        redisTemplate.opsForHash().putAll(key, otpData);
        redisTemplate.expire(key, otpConfig.getExpiry().getSeconds(), TimeUnit.SECONDS);

        setCooldown(email);
        incrementRateLimits(email);
        
        // Clear failed attempts
        redisTemplate.delete(getFailedAttemptKey(email));

        return otp;
    }

    public boolean verifyOtp(OTPType type, String email, String otpInput) {
        String key = getOtpKey(type, email);
        if (!Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            return false;
        }

        String storedHash = (String) redisTemplate.opsForHash().get(key, "hash");
        if (storedHash == null) return false;

        String failedKey = getFailedAttemptKey(email);
        Integer attempts = (Integer) redisTemplate.opsForValue().get(failedKey);
        if (attempts == null) attempts = 0;

        if (attempts >= otpConfig.getMaxAttempts()) {
            throw new RuntimeException("Maximum OTP attempts reached.");
        }

        if (passwordEncoder.matches(otpInput, storedHash)) {
            // Success
            redisTemplate.delete(key);
            redisTemplate.delete(failedKey);
            return true;
        } else {
            // Failure
            attempts++;
            redisTemplate.opsForValue().set(failedKey, attempts, otpConfig.getExpiry().getSeconds(), TimeUnit.SECONDS);
            if (attempts >= otpConfig.getMaxAttempts()) {
                redisTemplate.delete(key); // lock out this OTP
            }
            return false;
        }
    }

    public String resendOtp(OTPType type, String email) {
        String key = getOtpKey(type, email);
        if (!Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            throw new RuntimeException("No active OTP session to resend.");
        }
        
        checkCooldown(email);

        Integer resends = (Integer) redisTemplate.opsForHash().get(key, "resends");
        if (resends == null) resends = 0;
        if (resends >= otpConfig.getMaxResend()) {
            throw new RuntimeException("Maximum resend limit reached.");
        }

        String newOtp = generateNumericOtp(otpConfig.getLength());
        String hashedOtp = passwordEncoder.encode(newOtp);

        redisTemplate.opsForHash().put(key, "hash", hashedOtp);
        redisTemplate.opsForHash().put(key, "resends", resends + 1);
        redisTemplate.expire(key, otpConfig.getExpiry().getSeconds(), TimeUnit.SECONDS);
        
        setCooldown(email);
        return newOtp;
    }

    public void createResetSession(String email) {
        String sessionId = UUID.randomUUID().toString();
        String key = getResetSessionKey(email);
        Map<String, Object> sessionData = new HashMap<>();
        sessionData.put("sessionId", sessionId);
        sessionData.put("verified", true);
        
        redisTemplate.opsForHash().putAll(key, sessionData);
        redisTemplate.expire(key, 5, TimeUnit.MINUTES);
    }

    public boolean validateResetSession(String email) {
        String key = getResetSessionKey(email);
        return Boolean.TRUE.equals(redisTemplate.hasKey(key)) && 
               Boolean.TRUE.equals(redisTemplate.opsForHash().get(key, "verified"));
    }

    public void cleanupOtpFlow(String email) {
        redisTemplate.delete(getResetSessionKey(email));
        redisTemplate.delete(getFailedAttemptKey(email));
        for (OTPType type : OTPType.values()) {
            redisTemplate.delete(getOtpKey(type, email));
        }
    }

    private void checkRateLimits(String email) {
        String hourlyKey = getRateLimitKey(email, "hourly");
        Integer hourlyCount = (Integer) redisTemplate.opsForValue().get(hourlyKey);
        if (hourlyCount != null && hourlyCount >= otpConfig.getHourlyLimit()) {
            throw new RuntimeException("Hourly OTP request limit exceeded.");
        }

        String dailyKey = getRateLimitKey(email, "daily");
        Integer dailyCount = (Integer) redisTemplate.opsForValue().get(dailyKey);
        if (dailyCount != null && dailyCount >= otpConfig.getDailyLimit()) {
            throw new RuntimeException("Daily OTP request limit exceeded.");
        }
    }

    private void incrementRateLimits(String email) {
        String hourlyKey = getRateLimitKey(email, "hourly");
        if (Boolean.TRUE.equals(redisTemplate.hasKey(hourlyKey))) {
            redisTemplate.opsForValue().increment(hourlyKey);
        } else {
            redisTemplate.opsForValue().set(hourlyKey, 1, 1, TimeUnit.HOURS);
        }

        String dailyKey = getRateLimitKey(email, "daily");
        if (Boolean.TRUE.equals(redisTemplate.hasKey(dailyKey))) {
            redisTemplate.opsForValue().increment(dailyKey);
        } else {
            redisTemplate.opsForValue().set(dailyKey, 1, 1, TimeUnit.DAYS);
        }
    }

    private void checkCooldown(String email) {
        if (Boolean.TRUE.equals(redisTemplate.hasKey(getCooldownKey(email)))) {
            throw new RuntimeException("Please wait 60 seconds before requesting a new OTP.");
        }
    }

    private void setCooldown(String email) {
        redisTemplate.opsForValue().set(getCooldownKey(email), "cooldown", otpConfig.getResendCooldown().getSeconds(), TimeUnit.SECONDS);
    }

    private String generateNumericOtp(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < length; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }
}
