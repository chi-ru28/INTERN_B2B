package com.b2becommerce.identityservice.service;

import com.b2becommerce.identityservice.entity.AccountStatus;
import com.b2becommerce.identityservice.entity.Role;
import com.b2becommerce.identityservice.entity.UserCredential;
import com.b2becommerce.identityservice.repository.RoleRepository;
import com.b2becommerce.identityservice.repository.UserCredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserCredentialRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        Optional<UserCredential> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            UserCredential user = new UserCredential();
            user.setName(name);
            user.setEmail(email);
            // Generate a random secure password for OAuth2 users
            user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
            
            Role customerRole = roleRepository.findByName("ROLE_CUSTOMER")
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setName("ROLE_CUSTOMER");
                        role.setDescription("Default customer role");
                        return roleRepository.save(role);
                    });
                    
            user.setRoles(Collections.singleton(customerRole));
            
            // Auto-verify emails from Google OAuth
            user.setStatus(AccountStatus.ACTIVE);
            
            userRepository.save(user);
        }

        return oAuth2User;
    }
}
