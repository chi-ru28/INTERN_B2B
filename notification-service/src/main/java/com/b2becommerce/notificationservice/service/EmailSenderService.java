package com.b2becommerce.notificationservice.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings("null")
public class EmailSenderService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    public void sendOrderConfirmationEmail(String to, String orderNumber) {
        log.info("Sending Order Confirmation email to {}", to);
        try {
            Context context = new Context();
            context.setVariable("orderNumber", orderNumber);
            
            String htmlContent = templateEngine.process("order-confirmation", context);
            
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setTo(to);
            helper.setSubject("Your B2B Order has been Confirmed!");
            helper.setText(htmlContent, true);
            
            javaMailSender.send(message);
            log.info("Order confirmation email successfully sent to {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send order confirmation email to {}", to, e);
        }
    }

    public void sendOrderCancellationEmail(String to, String orderNumber, String reason) {
        log.info("Sending Order Cancellation email to {}", to);
        try {
            Context context = new Context();
            context.setVariable("orderNumber", orderNumber);
            context.setVariable("reason", reason);
            
            String htmlContent = templateEngine.process("order-cancellation", context);
            
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setTo(to);
            helper.setSubject("Update Regarding Your B2B Order: Cancelled");
            helper.setText(htmlContent, true);
            
            javaMailSender.send(message);
            log.info("Order cancellation email successfully sent to {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send order cancellation email to {}", to, e);
        }
    }
}
