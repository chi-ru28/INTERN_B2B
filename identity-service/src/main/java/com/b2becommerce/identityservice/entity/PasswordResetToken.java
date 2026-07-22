package com.b2becommerce.identityservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "password_reset_tokens")
@Data
@NoArgsConstructor
@Deprecated
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @OneToOne(targetEntity = UserCredential.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private UserCredential user;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    private boolean used = false;
    
    private String ipAddress;

    @Deprecated
    public PasswordResetToken(String token, UserCredential user, String ipAddress) {
        this.token = token;
        this.user = user;
        this.ipAddress = ipAddress;
        this.createdAt = LocalDateTime.now();
        // 15 minute expiry per requirements
        this.expiresAt = LocalDateTime.now().plusMinutes(15);
    }

}
