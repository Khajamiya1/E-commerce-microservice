package com.Ecommerce.ProductService.dto;
import com.Ecommerce.ProductService.entity.Category;
import lombok.Data;

@Data
public class ProductResponseDTO {
    private Long productId;
    private String name;
    private Double price;
    private Category category;
    private Boolean available;
}

