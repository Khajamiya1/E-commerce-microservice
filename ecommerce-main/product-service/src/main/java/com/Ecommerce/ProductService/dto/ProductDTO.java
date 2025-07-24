package com.Ecommerce.ProductService.dto;
import com.Ecommerce.ProductService.entity.Category;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private Long productId;
    private String name;
    private Double price;
    private Category category;
    private Boolean available;
}
