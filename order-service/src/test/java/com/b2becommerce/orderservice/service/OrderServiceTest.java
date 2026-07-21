package com.b2becommerce.orderservice.service;

import com.b2becommerce.orderservice.entity.Order;
import com.b2becommerce.orderservice.entity.OrderLineItems;
import com.b2becommerce.orderservice.event.OrderPlacedEvent;
import com.b2becommerce.orderservice.repository.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("null")
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private OrderService orderService;

    private Order order;

    @BeforeEach
    void setUp() {
        OrderLineItems lineItem = new OrderLineItems();
        lineItem.setSkuCode("SKU-123");
        lineItem.setPrice(BigDecimal.valueOf(100));
        lineItem.setQuantity(1);

        order = new Order();
        order.setOrderLineItemsList(List.of(lineItem));
    }

    @Test
    void testPlaceOrder_Success() {
        String result = orderService.placeOrder(order, "customer@example.com");

        assertEquals("Order Placed Successfully and is PENDING verification", result);
        verify(orderRepository, times(1)).save(order);
        verify(kafkaTemplate, times(1)).send(eq("orderTopic"), any(OrderPlacedEvent.class));
    }
}
