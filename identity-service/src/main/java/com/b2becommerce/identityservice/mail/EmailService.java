package com.b2becommerce.identityservice.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public void sendVerificationEmail(String toEmail, String name, String token) {
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("verificationLink", "http://localhost:3000/verify-email?token=" + token);
        
        String process = templateEngine.process("email/verify-email", context);
        sendHtmlEmail(toEmail, "Verify your B2B Enterprise Account", process);
    }

    public void sendPasswordResetEmail(String toEmail, String name, String token) {
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("resetLink", "http://localhost:3000/reset-password?token=" + token);
        
        String process = templateEngine.process("email/reset-password", context);
        sendHtmlEmail(toEmail, "Reset Your Password - B2B Enterprise", process);
    }

    public void sendNewLoginAlert(String toEmail, String name, String ipAddress, String time) {
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("ipAddress", ipAddress);
        context.setVariable("time", time);
        
        String process = templateEngine.process("email/new-login", context);
        sendHtmlEmail(toEmail, "New Login Detected - B2B Enterprise", process);
    }

    public void sendOtpEmail(String toEmail, String name, String otp, String ipAddress, String browser, String os, String country, String time) {
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("otp", otp);
        context.setVariable("ipAddress", ipAddress);
        context.setVariable("browser", browser);
        context.setVariable("os", os);
        context.setVariable("country", country);
        context.setVariable("time", time);
        
        String process = templateEngine.process("email/otp-email", context);
        sendHtmlEmail(toEmail, "Your Password Reset OTP - B2B Enterprise", process);
    }

    private void sendHtmlEmail(String to, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            
            mailSender.send(message);
        } catch (MessagingException e) {
            System.err.println("Failed to send email to " + to + ": " + e.getMessage());
        }
    }
}
