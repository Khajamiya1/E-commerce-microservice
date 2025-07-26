package com.Ecommerce.OrderService.dto;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductResponseDTO  {
    private Long id;
    private String name;
    private BigDecimal price;
    private boolean available;
}
