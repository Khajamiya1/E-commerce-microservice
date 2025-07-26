package com.Ecommerce.OrderService.client;


import com.Ecommerce.OrderService.dto.ProductResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "product-service")
public interface ProductClient {

    @GetMapping("/api/products/{productId}")
    ProductResponseDTO getProductById(@PathVariable("productId") Long productId);
}