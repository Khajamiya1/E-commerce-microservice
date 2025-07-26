package com.Ecommerce.ProductService.repository;

import com.Ecommerce.ProductService.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
