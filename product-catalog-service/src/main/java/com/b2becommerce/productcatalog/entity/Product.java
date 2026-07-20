package com.b2becommerce.productcatalog.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

@Document(collection = "products")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product implements Serializable {

    @Id
    private String id;
    private String skuCode;
    private String name;
    private String description;
    private BigDecimal price;
    
    // Using Map allows for polymorphic, flexible product attributes 
    // (e.g. dimensions, color, weight) which is why we use MongoDB
    private Map<String, Object> attributes;
}
