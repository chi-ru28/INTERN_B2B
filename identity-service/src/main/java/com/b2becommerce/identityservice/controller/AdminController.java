package com.b2becommerce.identityservice.controller;

import com.b2becommerce.identityservice.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth/admin")
public class AdminController {

    @Autowired
    private AuditLogRepository auditLogRepository;

    @GetMapping("/otp-stats")
    public Map<String, Object> getOtpStats() {
        LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);

        long requestsToday = auditLogRepository.countByEventTypeAndTimestampAfter("OTP_REQUESTED", today);
        long verifiedToday = auditLogRepository.countByEventTypeAndTimestampAfter("OTP_VERIFIED", today);
        long failuresToday = auditLogRepository.countByEventTypeAndTimestampAfter("OTP_FAILED", today);
        long resetsToday = auditLogRepository.countByEventTypeAndTimestampAfter("PASSWORD_RESET", today);

        Map<String, Object> stats = new HashMap<>();
        stats.put("requestsToday", requestsToday);
        stats.put("verifiedToday", verifiedToday);
        stats.put("failuresToday", failuresToday);
        stats.put("resetsToday", resetsToday);
        
        return stats;
    }
}
