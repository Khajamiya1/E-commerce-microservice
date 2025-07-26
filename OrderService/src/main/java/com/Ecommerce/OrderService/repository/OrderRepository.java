package com.Ecommerce.OrderService.repository;

import com.Ecommerce.OrderService.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
//import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCustomerId(Long customerId);
}
