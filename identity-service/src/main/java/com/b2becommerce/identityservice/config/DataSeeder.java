package com.b2becommerce.identityservice.config;

import com.b2becommerce.identityservice.entity.AccountStatus;
import com.b2becommerce.identityservice.entity.Role;
import com.b2becommerce.identityservice.entity.UserCredential;
import com.b2becommerce.identityservice.repository.RoleRepository;
import com.b2becommerce.identityservice.repository.UserCredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UserCredentialRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        Role customerRole = roleRepository.findByName("ROLE_CUSTOMER")
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName("ROLE_CUSTOMER");
                    role.setDescription("Default customer role");
                    return roleRepository.save(role);
                });

        String email = "patelruchi2830@gmail.com";
        if (!userRepository.existsByEmail(email)) {
            UserCredential user = new UserCredential();
            user.setName("Demo User");
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode("password123"));
            user.setRoles(Collections.singleton(customerRole));
            user.setStatus(AccountStatus.ACTIVE);
            userRepository.save(user);
            System.out.println("Seeded demo user: " + email);
        }
    }
}
