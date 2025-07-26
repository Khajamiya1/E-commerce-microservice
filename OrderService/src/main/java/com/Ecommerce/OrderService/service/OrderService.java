package com.Ecommerce.OrderService.service;

import com.Ecommerce.OrderService.dto.OrderRequestDTO;
import com.Ecommerce.OrderService.dto.OrderResponseDTO;
import com.Ecommerce.OrderService.entity.Order;

import java.util.List;

public interface OrderService {

//    Order createOrder(@Valid OrderRequestDTO order);

//    Order getOrderById(UUID orderId);

//    List<Order> getOrdersByCustomerId(UUID customerId);

//    Order updateOrder(UUID orderId, Order updatedOrder);

    void softDeleteOrder(Long orderId);

//    Order cancelOrder(UUID orderId);

    List<OrderResponseDTO> getAllOrders();

    OrderResponseDTO createOrder(Order requestDTO);

    OrderResponseDTO getOrderById(Long orderId);

    List<OrderResponseDTO> getOrdersByCustomerId(Long customerId);

    OrderResponseDTO cancelOrder(Long orderId);

    OrderResponseDTO updateOrder(Long orderId, OrderRequestDTO requestDTO);

    OrderResponseDTO retryPayment(Long orderId);


}
