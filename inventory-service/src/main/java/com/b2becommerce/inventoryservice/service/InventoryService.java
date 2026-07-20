package com.b2becommerce.inventoryservice.service;

import com.b2becommerce.inventoryservice.repository.InventoryRepository;
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
    public void deductInventory(String orderNumber) {
        // In a real scenario, we would parse the items and deduct specific quantities.
        // For this scaffolding, we simulate the deduction process.
        log.info("Deducting inventory for order: {}", orderNumber);
    }
}
