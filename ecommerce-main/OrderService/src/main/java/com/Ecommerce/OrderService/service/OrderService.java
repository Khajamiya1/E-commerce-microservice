package com.Ecommerce.OrderService.service;

import com.Ecommerce.OrderService.dto.OrderRequestDTO;
import com.Ecommerce.OrderService.dto.OrderResponseDTO;

import java.util.List;
import java.util.UUID;

public interface OrderService {

//    Order createOrder(@Valid OrderRequestDTO order);

//    Order getOrderById(UUID orderId);

//    List<Order> getOrdersByCustomerId(UUID customerId);

//    Order updateOrder(UUID orderId, Order updatedOrder);

    void softDeleteOrder(Long orderId);

//    Order cancelOrder(UUID orderId);

    List<OrderResponseDTO> getAllOrders();

    OrderResponseDTO createOrder(OrderRequestDTO requestDTO);

    OrderResponseDTO getOrderById(Long orderId);

    List<OrderResponseDTO> getOrdersByCustomerId(Long customerId);

    OrderResponseDTO cancelOrder(Long orderId);

    OrderResponseDTO updateOrder(Long orderId, OrderRequestDTO requestDTO);

    OrderResponseDTO retryPayment(Long orderId);


}
