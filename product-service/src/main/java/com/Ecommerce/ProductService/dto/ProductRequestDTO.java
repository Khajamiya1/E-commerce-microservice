package com.Ecommerce.ProductService.dto;
import com.Ecommerce.ProductService.entity.Category;
import lombok.Data;

import jakarta.validation.constraints.*;

@Data
public class ProductRequestDTO {

    @NotBlank(message = "Product name must not be blank")
    private String name;

    @NotNull(message = "Price must not be null")
    @Positive(message = "Price must be greater than 0")
    private Double price;

    @NotNull(message = "Category must not be null")
    private Category category;

    @NotNull(message = "Availability must not be null")
    private Boolean available;
}