package com.b2becommerce.inventoryservice.listener;

import com.b2becommerce.inventoryservice.event.OrderPlacedEvent;
import com.b2becommerce.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderEventListener {

    private final InventoryService inventoryService;

    @KafkaListener(topics = "orderTopic", groupId = "inventoryId")
    public void handleOrderPlacedEvent(OrderPlacedEvent orderPlacedEvent) {
        log.info("Received OrderPlacedEvent for Order - {}", orderPlacedEvent.getOrderNumber());
        inventoryService.deductInventory(orderPlacedEvent.getOrderNumber());
    }
}
