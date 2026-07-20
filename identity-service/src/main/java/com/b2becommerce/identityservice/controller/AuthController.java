package com.b2becommerce.identityservice.controller;

import com.b2becommerce.identityservice.entity.UserCredential;
import com.b2becommerce.identityservice.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtService jwtService;

    // In a real application, you would inject a service that hashes the password
    // and saves the UserCredential to the database.
    @PostMapping("/register")
    public String register(@RequestBody UserCredential user) {
        // userCredentialService.saveUser(user);
        return "User registration is successful (Mock)";
    }

    // In a real application, you would validate credentials against the database here
    @GetMapping("/token")
    public String getToken(@RequestParam("username") String username) {
        return jwtService.generateToken(username);
    }

    @GetMapping("/validate")
    public String validateToken(@RequestParam("token") String token) {
        jwtService.validateToken(token);
        return "Token is valid";
    }
}
