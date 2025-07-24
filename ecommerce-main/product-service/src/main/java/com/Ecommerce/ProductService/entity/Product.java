package com.Ecommerce.ProductService.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import jakarta.validation.constraints.*;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @NotBlank(message = "Product name is mandatory")
    private String name;

    @NotNull(message = "Price is mandatory")
    @Min(value = 1, message = "Price must be greater than 0")
    private Double price;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Category is mandatory")
    private Category category;

    @NotNull(message = "Availability must be specified")
    private Boolean available;
}
