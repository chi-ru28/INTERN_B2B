package com.b2becommerce.orderservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import com.b2becommerce.orderservice.dto.OrderLineItemDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderPlacedEvent {
    private String orderNumber;
    private String customerEmail;
    private List<OrderLineItemDto> items;
}
