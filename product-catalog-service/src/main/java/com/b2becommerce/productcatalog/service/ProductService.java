package com.b2becommerce.productcatalog.service;

import com.b2becommerce.productcatalog.entity.Product;
import com.b2becommerce.productcatalog.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.cache.annotation.CachePut;

@Service
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings("null")
public class ProductService {

    private final ProductRepository productRepository;

    @CacheEvict(value = "products_page", allEntries = true)
    public Product createProduct(Product product) {
        log.info("Creating product with SKU: {}", product.getSkuCode());
        return productRepository.save(product);
    }

    @Cacheable(value = "products_page", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<Product> getAllProducts(Pageable pageable) {
        log.info("Fetching all products from database with pagination");
        return productRepository.findAll(pageable);
    }

    public Page<Product> searchProducts(String category, String name, Pageable pageable) {
        log.info("Searching products by category: {}, name: {}", category, name);
        if (category != null && !category.isEmpty() && name != null && !name.isEmpty()) {
            return productRepository.findByCategoryIgnoreCaseAndNameContainingIgnoreCase(category, name, pageable);
        } else if (category != null && !category.isEmpty()) {
            return productRepository.findByCategoryIgnoreCase(category, pageable);
        } else if (name != null && !name.isEmpty()) {
            return productRepository.findByNameContainingIgnoreCase(name, pageable);
        }
        return getAllProducts(pageable);
    }

    @Cacheable(value = "products", key = "#skuCode")
    public Optional<Product> getProductBySku(String skuCode) {
        log.info("Fetching product by SKU from database: {}", skuCode);
        return productRepository.findBySkuCode(skuCode);
    }

    @CachePut(value = "products", key = "#skuCode")
    @CacheEvict(value = "products_page", allEntries = true)
    public Product updateProduct(String skuCode, Product updatedProduct) {
        log.info("Updating product with SKU: {}", skuCode);
        return productRepository.findBySkuCode(skuCode).map(existingProduct -> {
            existingProduct.setName(updatedProduct.getName());
            existingProduct.setDescription(updatedProduct.getDescription());
            existingProduct.setPrice(updatedProduct.getPrice());
            existingProduct.setCategory(updatedProduct.getCategory());
            existingProduct.setBrand(updatedProduct.getBrand());
            existingProduct.setStockStatus(updatedProduct.getStockStatus());
            if (updatedProduct.getAttributes() != null) {
                existingProduct.setAttributes(updatedProduct.getAttributes());
            }
            return productRepository.save(existingProduct);
        }).orElseThrow(() -> new RuntimeException("Product not found with SKU: " + skuCode));
    }

    @CacheEvict(value = {"products", "products_page"}, allEntries = true)
    public void deleteProduct(String skuCode) {
        log.info("Deleting product with SKU: {}", skuCode);
        productRepository.deleteBySkuCode(skuCode);
    }
}
