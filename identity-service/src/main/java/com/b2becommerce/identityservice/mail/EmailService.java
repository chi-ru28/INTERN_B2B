package com.b2becommerce.identityservice.mail;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.b2becommerce.identityservice.exception.SmtpAuthException;

@Service
@SuppressWarnings("null")
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @org.springframework.beans.factory.annotation.Value("${spring.mail.username:}")
    private String mailUsername;

    @org.springframework.beans.factory.annotation.Value("${spring.mail.password:}")
    private String mailPassword;

    private void checkSmtpConfig() {
        if ("dev".equals(mailUsername)) return;
        if (mailUsername == null || mailUsername.isEmpty() || mailPassword == null || mailPassword.isEmpty()) {
            throw new com.b2becommerce.identityservice.exception.SmtpConfigurationMissingException("SMTP credentials are not configured.");
        }
    }

    public void sendVerificationEmail(String toEmail, String name, String token) {
        checkSmtpConfig();
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("verificationLink", "http://localhost:3000/verify-email?token=" + token);
        
        String process = templateEngine.process("email/verify-email", context);
        sendHtmlEmail(toEmail, "Verify your B2B Enterprise Account", process);
    }

    public void sendPasswordResetEmail(String toEmail, String name, String token) {
        checkSmtpConfig();
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("resetLink", "http://localhost:3000/reset-password?token=" + token);
        
        String process = templateEngine.process("email/reset-password", context);
        sendHtmlEmail(toEmail, "Reset Your Password - B2B Enterprise", process);
    }

    public void sendNewLoginAlert(String toEmail, String name, String ipAddress, String time) {
        checkSmtpConfig();
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("ipAddress", ipAddress);
        context.setVariable("time", time);
        
        String process = templateEngine.process("email/new-login", context);
        sendHtmlEmail(toEmail, "New Login Detected - B2B Enterprise", process);
    }

    public void sendOtpEmail(String toEmail, String name, String otp, String ipAddress, String browser, String os, String country, String time) {
        checkSmtpConfig();
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
        log.info("OTP email sent successfully to {}", toEmail);
    }

    private void sendHtmlEmail(String to, String subject, String htmlBody) {
        if ("dev".equals(mailUsername)) {
            log.info("DEV MODE: Mock sending email to {} with subject: {}", to, subject);
            return;
        }
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            
            mailSender.send(message);
        } catch (org.springframework.mail.MailAuthenticationException e) {
            log.error("SMTP authentication failed when sending to {}: {}", to, e.getMessage());
            throw new SmtpAuthException("Unable to authenticate with Gmail SMTP. Check your credentials.");
        } catch (org.springframework.mail.MailSendException e) {
            log.error("SMTP server unavailable when sending to {}: {}", to, e.getMessage());
            throw new SmtpAuthException("Unable to send email. Mail server might be unavailable.");
        } catch (jakarta.mail.MessagingException e) {
            log.error("Messaging exception occurred when sending to {}: {}", to, e.getMessage());
            throw new SmtpAuthException("Error constructing the email message.");
        } catch (Exception e) {
            log.error("Unexpected error occurred when sending to {}: {}", to, e.getMessage());
            throw new SmtpAuthException("Unable to authenticate with Gmail SMTP or send email.");
        }
    }
}
