package com.b2becommerce.identityservice.repository;

import com.b2becommerce.identityservice.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findByEmailAndTimestampAfter(String email, LocalDateTime timestamp);
    long countByEventTypeAndTimestampAfter(String eventType, LocalDateTime timestamp);
}
