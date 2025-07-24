package com.Ecommerce.OrderService.helper;

import com.Ecommerce.OrderService.client.ProductClient;
import com.Ecommerce.OrderService.dto.ProductResponseDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class ProductServiceHelper {

    private final ProductClient productClient;

    @CircuitBreaker(name = "productService", fallbackMethod = "handleProductServiceFallback")
    public ProductResponseDTO getProductDetails(Long productId) {
        return productClient.getProductById(productId);
    }

    public ProductResponseDTO handleProductServiceFallback(Long productId, Throwable ex) {
        System.out.println("⚠️ Fallback for ProductService: " + ex.getMessage());
        ProductResponseDTO fallback = new ProductResponseDTO();
        fallback.setId(productId);
        fallback.setAvailable(false);
        fallback.setName("Unavailable");
        fallback.setPrice(BigDecimal.ZERO);
        return fallback;
    }
}
