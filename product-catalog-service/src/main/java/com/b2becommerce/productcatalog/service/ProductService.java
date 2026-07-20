package com.b2becommerce.productcatalog.service;

import com.b2becommerce.productcatalog.entity.Product;
import com.b2becommerce.productcatalog.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    @CacheEvict(value = "products", allEntries = true)
    public Product createProduct(Product product) {
        log.info("Creating product with SKU: {}", product.getSkuCode());
        return productRepository.save(product);
    }

    @Cacheable(value = "products")
    public List<Product> getAllProducts() {
        log.info("Fetching all products from database");
        return productRepository.findAll();
    }

    @Cacheable(value = "products", key = "#skuCode")
    public Optional<Product> getProductBySku(String skuCode) {
        log.info("Fetching product by SKU from database: {}", skuCode);
        return productRepository.findBySkuCode(skuCode);
    }
}
