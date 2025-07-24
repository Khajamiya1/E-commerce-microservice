package com.Ecommerce.ProductService.service;

import com.Ecommerce.ProductService.dto.ProductDTO;
import com.Ecommerce.ProductService.dto.ProductRequestDTO;
import com.Ecommerce.ProductService.dto.ProductResponseDTO;

import java.util.List;

public interface ProductService {
    ProductResponseDTO createProduct(ProductDTO productRequestDTO);
    ProductResponseDTO getProductById(Long productId);
    List<ProductResponseDTO> getAllProducts();
    ProductResponseDTO updateProduct(Long productId, ProductRequestDTO productRequestDTO);
    void deleteProduct(Long productId);
    public ProductDTO getProductDetails(Long id);
}
