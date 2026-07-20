package com.b2becommerce.orderservice.service;

import com.b2becommerce.orderservice.client.InventoryClient;
import com.b2becommerce.orderservice.entity.Order;
import com.b2becommerce.orderservice.entity.OrderLineItems;
import com.b2becommerce.orderservice.event.OrderPlacedEvent;
import com.b2becommerce.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("null")
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    @Mock
    private InventoryClient inventoryClient;

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
        when(inventoryClient.areInStock(anyList())).thenReturn(Map.of("SKU-123", true));

        String result = orderService.placeOrder(order);

        assertEquals("Order Placed Successfully", result);
        verify(orderRepository, times(1)).save(order);
        verify(kafkaTemplate, times(1)).send(eq("orderTopic"), any(OrderPlacedEvent.class));
    }

    @Test
    void testPlaceOrder_OutOfStock() {
        when(inventoryClient.areInStock(anyList())).thenReturn(Map.of("SKU-123", false));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.placeOrder(order);
        });

        assertEquals("Product is not in stock, please try again later", exception.getMessage());
        verify(orderRepository, never()).save(any(Order.class));
        verify(kafkaTemplate, never()).send(anyString(), any(OrderPlacedEvent.class));
    }

    @Test
    void testPlaceOrder_CircuitBreakerFallback() {
        // Fallback logic test
        RuntimeException ex = new RuntimeException("Inventory Service Down");
        String result = orderService.fallbackMethod(order, ex);
        
        assertEquals("Oops! Something went wrong, please order after some time!", result);
    }
}
