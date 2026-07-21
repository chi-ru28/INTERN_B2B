package com.b2becommerce.orderservice.controller;

import com.b2becommerce.orderservice.entity.Order;
import com.b2becommerce.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String placeOrder(@RequestBody Order order, @RequestHeader("loggedInUser") String email) {
        return orderService.placeOrder(order, email);
    }
    
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Order> getOrders() {
        return orderService.getAllOrders();
    }
}
