package com.b2becommerce.identityservice.repository;

import com.b2becommerce.identityservice.entity.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserCredentialRepository extends JpaRepository<UserCredential, Long> {
    Optional<UserCredential> findByEmail(String email);
    boolean existsByEmail(String email);
}
