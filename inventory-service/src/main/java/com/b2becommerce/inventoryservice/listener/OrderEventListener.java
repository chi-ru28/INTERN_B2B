package com.b2becommerce.inventoryservice.listener;

import com.b2becommerce.inventoryservice.event.OrderPlacedEvent;
import com.b2becommerce.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.b2becommerce.inventoryservice.event.InventoryDeductedEvent;
import com.b2becommerce.inventoryservice.event.InventoryFailedEvent;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderEventListener {

    private final InventoryService inventoryService;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "orderTopic", groupId = "inventoryId")
    public void handleOrderPlacedEvent(String message) {
        log.info("Received OrderPlacedEvent message: {}", message);
        OrderPlacedEvent orderPlacedEvent = null;
        try {
            orderPlacedEvent = objectMapper.readValue(message, OrderPlacedEvent.class);
            log.info("Parsed OrderPlacedEvent for Order - {}", orderPlacedEvent.getOrderNumber());
            
            // Try to deduct inventory atomically
            inventoryService.deductInventoryBatch(orderPlacedEvent.getItems());
            
            // If successful, publish InventoryDeductedEvent
            kafkaTemplate.send("inventoryTopic", new InventoryDeductedEvent(orderPlacedEvent.getOrderNumber()));
            log.info("Inventory successfully deducted for Order - {}", orderPlacedEvent.getOrderNumber());
            
        } catch (com.b2becommerce.inventoryservice.exception.InsufficientStockException ex) {
            log.warn("Insufficient stock for Order - {}. Reason: {}", orderPlacedEvent != null ? orderPlacedEvent.getOrderNumber() : "unknown", ex.getMessage());
            if (orderPlacedEvent != null) {
                kafkaTemplate.send("inventoryTopic", new InventoryFailedEvent(orderPlacedEvent.getOrderNumber(), ex.getMessage()));
            }
        } catch (Exception ex) {
            log.error("Failed to process OrderPlacedEvent", ex);
            if (orderPlacedEvent != null) {
                kafkaTemplate.send("inventoryTopic", new InventoryFailedEvent(orderPlacedEvent.getOrderNumber(), "Internal Server Error during deduction"));
            }
        }
    }
}
