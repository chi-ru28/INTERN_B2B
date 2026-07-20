package com.b2becommerce.notificationservice.listener;

import com.b2becommerce.notificationservice.event.OrderPlacedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderEventListener {

    @KafkaListener(topics = "orderTopic", groupId = "notificationId")
    public void handleOrderPlacedEvent(OrderPlacedEvent orderPlacedEvent) {
        log.info("Received Notification for Order - {}", orderPlacedEvent.getOrderNumber());
        // Simulating sending email/sms to customer
        log.info("Sending confirmation email/SMS for Order Number: {}", orderPlacedEvent.getOrderNumber());
    }
}
