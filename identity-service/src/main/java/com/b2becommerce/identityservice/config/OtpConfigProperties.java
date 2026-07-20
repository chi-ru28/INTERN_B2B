package com.b2becommerce.identityservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import java.time.Duration;

@Configuration
@ConfigurationProperties(prefix = "app.security.otp")
public class OtpConfigProperties {
    private int length = 6;
    private Duration expiry = Duration.ofMinutes(5);
    private Duration resendCooldown = Duration.ofSeconds(60);
    private int maxAttempts = 5;
    private int maxResend = 3;
    private int hourlyLimit = 5;
    private int dailyLimit = 20;

    public int getLength() { return length; }
    public void setLength(int length) { this.length = length; }
    public Duration getExpiry() { return expiry; }
    public void setExpiry(Duration expiry) { this.expiry = expiry; }
    public Duration getResendCooldown() { return resendCooldown; }
    public void setResendCooldown(Duration resendCooldown) { this.resendCooldown = resendCooldown; }
    public int getMaxAttempts() { return maxAttempts; }
    public void setMaxAttempts(int maxAttempts) { this.maxAttempts = maxAttempts; }
    public int getMaxResend() { return maxResend; }
    public void setMaxResend(int maxResend) { this.maxResend = maxResend; }
    public int getHourlyLimit() { return hourlyLimit; }
    public void setHourlyLimit(int hourlyLimit) { this.hourlyLimit = hourlyLimit; }
    public int getDailyLimit() { return dailyLimit; }
    public void setDailyLimit(int dailyLimit) { this.dailyLimit = dailyLimit; }
}
