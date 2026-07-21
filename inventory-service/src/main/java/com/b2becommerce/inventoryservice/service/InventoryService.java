package com.b2becommerce.inventoryservice.service;

import com.b2becommerce.inventoryservice.repository.InventoryRepository;
import com.b2becommerce.inventoryservice.exception.InventoryNotFoundException;
import com.b2becommerce.inventoryservice.exception.InsufficientStockException;
import com.b2becommerce.inventoryservice.entity.Inventory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public boolean isInStock(String skuCode) {
        log.info("Checking inventory for SKU: {}", skuCode);
        return inventoryRepository.findBySkuCode(skuCode)
                .map(inventory -> inventory.getQuantity() > 0)
                .orElse(false);
    }

    @Transactional(readOnly = true)
    public Map<String, Boolean> areInStock(List<String> skuCodes) {
        log.info("Checking inventory for multiple SKUs: {}", skuCodes);
        return inventoryRepository.findBySkuCodeIn(skuCodes).stream()
                .collect(Collectors.toMap(
                        inventory -> inventory.getSkuCode(),
                        inventory -> inventory.getQuantity() > 0,
                        (a, b) -> a || b
                ));
    }

    @Transactional
    public void deductInventory(String skuCode, int quantity) {
        log.info("Deducting {} inventory for SKU: {}", quantity, skuCode);
        Inventory inventory = inventoryRepository.findBySkuCode(skuCode)
                .orElseThrow(() -> new InventoryNotFoundException("Inventory not found for SKU: " + skuCode));

        if (inventory.getQuantity() - inventory.getReservedQuantity() < quantity) {
            throw new InsufficientStockException("Insufficient stock for SKU: " + skuCode);
        }

        inventory.setQuantity(inventory.getQuantity() - quantity);
        inventoryRepository.save(inventory);
    }

    @Transactional
    public void addStock(String skuCode, int quantity) {
        log.info("Adding {} inventory for SKU: {}", quantity, skuCode);
        Inventory inventory = inventoryRepository.findBySkuCode(skuCode)
                .orElseGet(() -> {
                    Inventory newInventory = new Inventory();
                    newInventory.setSkuCode(skuCode);
                    newInventory.setQuantity(0);
                    newInventory.setReservedQuantity(0);
                    return newInventory;
                });

        inventory.setQuantity(inventory.getQuantity() + quantity);
        inventoryRepository.save(inventory);
    }

    @Transactional
    public void reserveStock(String skuCode, int quantity) {
        log.info("Reserving {} inventory for SKU: {}", quantity, skuCode);
        Inventory inventory = inventoryRepository.findBySkuCode(skuCode)
                .orElseThrow(() -> new InventoryNotFoundException("Inventory not found for SKU: " + skuCode));

        if (inventory.getQuantity() - inventory.getReservedQuantity() < quantity) {
            throw new InsufficientStockException("Insufficient stock to reserve for SKU: " + skuCode);
        }

        inventory.setReservedQuantity(inventory.getReservedQuantity() + quantity);
        inventoryRepository.save(inventory);
    }

    @Transactional
    public void deductInventoryBatch(List<com.b2becommerce.inventoryservice.event.OrderLineItemDto> items) {
        log.info("Attempting batch deduction for {} items", items.size());
        for (com.b2becommerce.inventoryservice.event.OrderLineItemDto item : items) {
            deductInventory(item.getSkuCode(), item.getQuantity());
        }
    }
}
