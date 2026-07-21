package com.b2becommerce.inventoryservice.service;

import com.b2becommerce.inventoryservice.entity.Inventory;
import com.b2becommerce.inventoryservice.repository.InventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InventoryServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private InventoryService inventoryService;

    private Inventory inventory1;
    private Inventory inventory2;

    @BeforeEach
    void setUp() {
        inventory1 = new Inventory(1L, "SKU-123", 10, 0);
        inventory2 = new Inventory(2L, "SKU-456", 0, 0);
    }

    @Test
    void testAreInStock() {
        when(inventoryRepository.findBySkuCodeIn(anyList()))
                .thenReturn(List.of(inventory1, inventory2));

        Map<String, Boolean> result = inventoryService.areInStock(List.of("SKU-123", "SKU-456"));

        assertEquals(2, result.size());
        assertTrue(result.get("SKU-123"));
        assertEquals(false, result.get("SKU-456"));
    }

    @Test
    void testDeductInventory() {
        when(inventoryRepository.findBySkuCode("SKU-123")).thenReturn(java.util.Optional.of(inventory1));
        inventoryService.deductInventory("SKU-123", 5);
        assertEquals(5, inventory1.getQuantity());
    }
}
