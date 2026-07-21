package com.b2becommerce.notificationservice.listener;

import com.b2becommerce.notificationservice.event.OrderCancelledEvent;
import com.b2becommerce.notificationservice.event.OrderConfirmedEvent;
import com.b2becommerce.notificationservice.service.EmailSenderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationEventListener {

    private final EmailSenderService emailSenderService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "notificationTopic", groupId = "notificationId")
    public void handleNotificationEvent(String message) {
        log.info("Received message on notificationTopic: {}", message);
        try {
            if (message.contains("reason")) {
                OrderCancelledEvent event = objectMapper.readValue(message, OrderCancelledEvent.class);
                emailSenderService.sendOrderCancellationEmail(event.getCustomerEmail(), event.getOrderNumber(), event.getReason());
            } else {
                OrderConfirmedEvent event = objectMapper.readValue(message, OrderConfirmedEvent.class);
                emailSenderService.sendOrderConfirmationEmail(event.getCustomerEmail(), event.getOrderNumber());
            }
        } catch (Exception e) {
            log.error("Failed to process notification event", e);
        }
    }
}
