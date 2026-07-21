package com.b2becommerce.inventoryservice.controller;

import com.b2becommerce.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/{skuCode}/stock")
    @ResponseStatus(HttpStatus.OK)
    public boolean isInStock(@PathVariable String skuCode) {
        return inventoryService.isInStock(skuCode);
    }

    // e.g. /api/inventory/check-stock?skuCode=sku-123&skuCode=sku-456
    @GetMapping("/check-stock")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Boolean> areInStock(@RequestParam List<String> skuCode) {
        return inventoryService.areInStock(skuCode);
    }

    @PostMapping("/{skuCode}/add")
    @ResponseStatus(HttpStatus.OK)
    public void addStock(@PathVariable String skuCode, @RequestParam int quantity) {
        inventoryService.addStock(skuCode, quantity);
    }

    @PostMapping("/{skuCode}/deduct")
    @ResponseStatus(HttpStatus.OK)
    public void deductStock(@PathVariable String skuCode, @RequestParam int quantity) {
        inventoryService.deductInventory(skuCode, quantity);
    }

    @PostMapping("/{skuCode}/reserve")
    @ResponseStatus(HttpStatus.OK)
    public void reserveStock(@PathVariable String skuCode, @RequestParam int quantity) {
        inventoryService.reserveStock(skuCode, quantity);
    }
}
