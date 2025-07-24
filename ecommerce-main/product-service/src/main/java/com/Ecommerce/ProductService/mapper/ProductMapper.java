package com.Ecommerce.ProductService.mapper;

import com.Ecommerce.ProductService.dto.ProductDTO;
import com.Ecommerce.ProductService.dto.ProductResponseDTO;
import com.Ecommerce.ProductService.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductDTO toDTO(Product product) {
        if (product == null) return null;

        ProductDTO dto = new ProductDTO();
        dto.setProductId(product.getProductId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setCategory(product.getCategory());
        dto.setAvailable(product.getAvailable());
        return dto;
    }

    public ProductResponseDTO toResponseDTO(Product product) {
        if (product == null) return null;

        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setProductId(product.getProductId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setCategory(product.getCategory());
        dto.setAvailable(product.getAvailable());
        return dto;
    }

    public Product toEntity(ProductDTO dto) {
        if (dto == null) return null;

        Product product = new Product();
        product.setProductId(dto.getProductId());
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setCategory(dto.getCategory());
        product.setAvailable(dto.getAvailable());
        return product;
    }
}
