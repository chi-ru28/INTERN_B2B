package com.b2becommerce.orderservice.service;

import com.b2becommerce.orderservice.entity.Order;
import com.b2becommerce.orderservice.event.OrderPlacedEvent;
import com.b2becommerce.orderservice.repository.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import com.b2becommerce.orderservice.entity.OrderStatus;
import com.b2becommerce.orderservice.dto.OrderLineItemDto;
import com.b2becommerce.orderservice.event.OrderConfirmedEvent;
import com.b2becommerce.orderservice.event.OrderCancelledEvent;
import com.b2becommerce.orderservice.event.InventoryDeductedEvent;
import com.b2becommerce.orderservice.event.InventoryFailedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Transactional
    public String placeOrder(Order order, String customerEmail) {
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setStatus(OrderStatus.PENDING);
        order.setCustomerEmail(customerEmail);
        orderRepository.save(order);
        
        log.info("Order Saved as PENDING. Publishing OrderPlacedEvent for order: {}", order.getOrderNumber());
        
        List<OrderLineItemDto> itemsDto = order.getOrderLineItemsList().stream()
                .map(item -> new OrderLineItemDto(item.getSkuCode(), item.getPrice(), item.getQuantity()))
                .collect(Collectors.toList());

        OrderPlacedEvent event = new OrderPlacedEvent(order.getOrderNumber(), customerEmail, itemsDto);
        log.info("Publishing OrderPlacedEvent for order: {}", order.getOrderNumber());
        kafkaTemplate.send("orderTopic", event);
        
        return "Order Placed Successfully and is PENDING verification";
    }

    @KafkaListener(topics = "inventoryTopic", groupId = "orderServiceId")
    @Transactional
    public void handleInventoryEvent(String message) {
        try {
            log.info("Received message on inventoryTopic: {}", message);
            // Since we might get Deducted or Failed event, we check if 'reason' exists
            if (message.contains("reason")) {
                InventoryFailedEvent failedEvent = objectMapper.readValue(message, InventoryFailedEvent.class);
                updateOrderStatus(failedEvent.getOrderNumber(), OrderStatus.CANCELLED);
                
                // Get email to send cancellation
                Order order = orderRepository.findByOrderNumber(failedEvent.getOrderNumber());
                if (order != null) {
                    log.info("Sending OrderCancelledEvent for order: {}", failedEvent.getOrderNumber());
                    kafkaTemplate.send("notificationTopic", new OrderCancelledEvent(failedEvent.getOrderNumber(), order.getCustomerEmail(), failedEvent.getReason()));
                }
            } else {
                InventoryDeductedEvent deductedEvent = objectMapper.readValue(message, InventoryDeductedEvent.class);
                updateOrderStatus(deductedEvent.getOrderNumber(), OrderStatus.CONFIRMED);
                
                // Send confirmation notification
                Order order = orderRepository.findByOrderNumber(deductedEvent.getOrderNumber());
                if (order != null) {
                    log.info("Sending OrderConfirmedEvent for order: {}", deductedEvent.getOrderNumber());
                    kafkaTemplate.send("notificationTopic", new OrderConfirmedEvent(deductedEvent.getOrderNumber(), order.getCustomerEmail()));
                }
            }
        } catch (Exception e) {
            log.error("Error processing inventory event", e);
        }
    }

    private void updateOrderStatus(String orderNumber, OrderStatus status) {
        Order order = orderRepository.findByOrderNumber(orderNumber);
        if (order != null) {
            order.setStatus(status);
            orderRepository.save(order);
            log.info("Order {} status updated to {}", orderNumber, status);
        } else {
            log.warn("Order {} not found to update status to {}", orderNumber, status);
        }
    }

    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
