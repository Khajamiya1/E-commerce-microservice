package com.Ecommerce.ProductService.service.impl;

import com.Ecommerce.ProductService.dto.ProductDTO;
import com.Ecommerce.ProductService.dto.ProductRequestDTO;
import com.Ecommerce.ProductService.dto.ProductResponseDTO;
import com.Ecommerce.ProductService.entity.Product;
import com.Ecommerce.ProductService.exception.ResourceNotFoundException;
import com.Ecommerce.ProductService.mapper.ProductMapper;
import com.Ecommerce.ProductService.repository.ProductRepository;
import com.Ecommerce.ProductService.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductResponseDTO createProduct(ProductDTO dto) {
        log.info("Creating product: {}", dto);

        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new IllegalArgumentException("Product name cannot be null or empty.");
        }

        if (dto.getPrice() == null || dto.getPrice().doubleValue() <= 0) {
            throw new IllegalArgumentException("Product price must be greater than zero.");
        }

        if (dto.getCategory() == null) {
            throw new IllegalArgumentException("Product category must be provided.");
        }

        if (!dto.getAvailable()) {
            log.warn("Creating product that is currently unavailable: {}", dto.getName());
        }

        Product product = productMapper.toEntity(dto);
        Product saved = productRepository.save(product);
        return productMapper.toResponseDTO(saved);
    }

    @Override
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO dto) {
        log.info("Updating product with ID: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));

        if (dto.getName() != null) product.setName(dto.getName());
        if (dto.getPrice() != null) product.setPrice(dto.getPrice());
        if (dto.getCategory() != null) product.setCategory(dto.getCategory());
        if (dto.getAvailable() != null) product.setAvailable(dto.getAvailable());

        Product updated = productRepository.save(product);
        return productMapper.toResponseDTO(updated);
    }

    @Override
    public ProductResponseDTO getProductById(Long id) {
        log.debug("Fetching product with ID: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));

        return productMapper.toResponseDTO(product);
    }

    @Override
    public List<ProductResponseDTO> getAllProducts() {
        log.debug("Fetching all products");
        return productRepository.findAll()
                .stream()
                .map(productMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteProduct(Long id) {
        log.info("Deleting product with ID: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));

        productRepository.delete(product);
    }

    @Override
    public ProductDTO getProductDetails(Long id) {
        log.debug("Fetching product details for internal use: ID {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));

        return productMapper.toDTO(product);
    }
}
