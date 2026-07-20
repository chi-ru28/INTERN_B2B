package com.b2becommerce.orderservice.service;

import com.b2becommerce.orderservice.client.InventoryClient;
import com.b2becommerce.orderservice.entity.Order;
import com.b2becommerce.orderservice.event.OrderPlacedEvent;
import com.b2becommerce.orderservice.repository.OrderRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;
    private final InventoryClient inventoryClient;

    @Transactional
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
    public String placeOrder(Order order) {
        
        List<String> skuCodes = order.getOrderLineItemsList().stream()
                .map(orderLineItem -> orderLineItem.getSkuCode())
                .collect(Collectors.toList());

        // Synchronous call to Inventory Service via Feign Client
        Map<String, Boolean> stockStatus = inventoryClient.areInStock(skuCodes);

        boolean allProductsInStock = stockStatus.values().stream().allMatch(Boolean::booleanValue);

        if (allProductsInStock) {
            order.setOrderNumber(UUID.randomUUID().toString());
            orderRepository.save(order);
            
            log.info("Order Saved, publishing OrderPlacedEvent for order: {}", order.getOrderNumber());
            
            // Publish event to Kafka Topic "orderTopic"
            kafkaTemplate.send("orderTopic", new OrderPlacedEvent(order.getOrderNumber()));
            
            return "Order Placed Successfully";
        } else {
            throw new IllegalArgumentException("Product is not in stock, please try again later");
        }
    }

    public String fallbackMethod(Order order, RuntimeException runtimeException) {
        log.error("Circuit breaker fallback triggered for order: {} due to {}", order, runtimeException.getMessage());
        return "Oops! Something went wrong, please order after some time!";
    }
}
