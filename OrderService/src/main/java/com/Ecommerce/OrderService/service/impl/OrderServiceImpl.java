package com.Ecommerce.OrderService.service.impl;

import com.Ecommerce.OrderService.client.PaymentClient;
import com.Ecommerce.OrderService.client.ProductClient;
import com.Ecommerce.OrderService.dto.*;
import com.Ecommerce.OrderService.entity.Order;
import com.Ecommerce.OrderService.entity.OrderItem;
import com.Ecommerce.OrderService.enums.OrderStatus;
import com.Ecommerce.OrderService.helper.PaymentServiceHelper;
import com.Ecommerce.OrderService.helper.ProductServiceHelper;
import com.Ecommerce.OrderService.repository.OrderRepository;
import com.Ecommerce.OrderService.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Getter
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

//private final OrderRepository orderRepository;
//
//
//    private final ProductClient productClient;
//
//    private final PaymentClient paymentClient;
//
//    private ProductServiceHelper productHelper;
//
//    private PaymentServiceHelper paymentHelper;

    private final OrderRepository orderRepository;
    private final ProductClient productClient;
    private final PaymentClient paymentClient;
    private final ProductServiceHelper productHelper;
    private final PaymentServiceHelper paymentHelper;

    @Override
    public List<OrderResponseDTO> getAllOrders(){

        List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponseDTO getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with ID: " + orderId));

        return mapToResponseDTO(order);
    }
/*
creating order
 */
    @Override
    public OrderResponseDTO createOrder(Order requestDTO) {



        if (requestDTO.getOrderItems() == null || requestDTO.getOrderItems().isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one product.");
        }

        // Build Order entity
        Order order = new Order();
        order.setCustomerId(requestDTO.getCustomerId());
        order.setStatus(OrderStatus.NEW);

        List<OrderItem> orderItems = requestDTO.getOrderItems().stream().map(itemDTO -> {

            /*
                Simulate product availability check - Ideally call Product Service here
                wrapped circuit breaker
             */

//            ProductResponseDTO product=productClient.getProductById(itemDTO.getProductId());
            ProductResponseDTO product = productHelper.getProductDetails(itemDTO.getProductId());

            if (product == null || !product.isAvailable()) {
                throw new IllegalArgumentException("Product with ID " + itemDTO.getProductId() + " is not available.");
            }

            if (product.getPrice().compareTo(itemDTO.getPrice()) != 0) {
                throw new IllegalArgumentException("Price mismatch for product ID " + itemDTO.getProductId());
            }



            OrderItem item = new OrderItem();
            item.setProductId(itemDTO.getProductId());
            item.setQuantity(itemDTO.getQuantity());
            item.setPrice(itemDTO.getPrice());
            item.setOrder(order);
            return item;
        }).collect(Collectors.toList());

        order.setOrderItems(orderItems);

        // Calculate total amount
        BigDecimal totalAmount = orderItems.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Total amount must be greater than zero.");
        }

        order.setTotalAmount(totalAmount);

        // Saving to database
        Order savedOrder = orderRepository.save(order);

        /*
           Calling PaymentService via Feign Client and wrapped circuit breaker around
         */
        PaymentRequestDTO paymentRequest = new PaymentRequestDTO();
        paymentRequest.setOrderId(savedOrder.getId());
        paymentRequest.setAmount(savedOrder.getTotalAmount());

//        PaymentResponseDTO paymentResponse = paymentClient.initiatePayment(paymentRequest);
        PaymentResponseDTO paymentResponse = paymentHelper.processPayment(paymentRequest);

        System.out.println("Payment Status: " + paymentResponse.getStatus());

        // update status if payment failed
        if (!"SUCCESS".equalsIgnoreCase(paymentResponse.getStatus())) {
            savedOrder.setStatus(OrderStatus.PAYMENT_FAILED); // Add this to your OrderStatus enum
            orderRepository.save(savedOrder);
        }


        // Map to Response DTO and return
        return mapToResponseDTO(savedOrder);
    }


    @Override
    public List<OrderResponseDTO> getOrdersByCustomerId(Long customerId) {
        List<Order> orders = orderRepository.findByCustomerId(customerId);

        return orders.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /*
    canceling order
     */

    @Override
    public OrderResponseDTO cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with ID: " + orderId));

        if (order.getStatus() == OrderStatus.CANCELED) {
            throw new IllegalStateException("Order is already canceled.");
        }

        if (order.getStatus() == OrderStatus.SHIPPED || order.getStatus() == OrderStatus.DELIVERED) {
            throw new IllegalStateException("Cannot cancel order that is already shipped or delivered.");
        }

        order.setStatus(OrderStatus.CANCELED);
        Order updatedOrder = orderRepository.save(order);

        return mapToResponseDTO(updatedOrder);
    }

/*
deleting order by ID
 */

    @Override
    public void softDeleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with ID: " + orderId));

        if (order.getStatus() == OrderStatus.SHIPPED || order.getStatus() == OrderStatus.DELIVERED) {
            throw new IllegalStateException("Cannot delete order that is already shipped or delivered.");
        }

        order.setStatus(OrderStatus.CANCELED);  // Soft delete using status
        orderRepository.save(order);
    }

    /**
     * updating order
     */
    @Override
    public OrderResponseDTO updateOrder(Long orderId, OrderRequestDTO requestDTO) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with ID: " + orderId));

        if (order.getStatus() != OrderStatus.NEW && order.getStatus() != OrderStatus.PROCESSING) {
            throw new IllegalStateException("Only orders in NEW or PROCESSING state can be updated.");
        }

        if (requestDTO.getOrderItems() == null || requestDTO.getOrderItems().isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one product.");
        }

        // Clear existing items - keeps the same collection reference
        order.getOrderItems().clear();

        // Add new items to the same list
        for (OrderItemDTO itemDTO : requestDTO.getOrderItems()) {

            //Calling Product service
//            ProductResponseDTO product = productClient.getProductById(itemDTO.getProductId());
            ProductResponseDTO product = productHelper.getProductDetails(itemDTO.getProductId());

            if (product == null || !product.isAvailable()) {
                throw new IllegalArgumentException("Product with ID " + itemDTO.getProductId() + " is not available.");
            }

            if (product.getPrice().compareTo(itemDTO.getPrice()) != 0) {
                throw new IllegalArgumentException("Price mismatch for product ID " + itemDTO.getProductId());
            }



            OrderItem item = new OrderItem();
            item.setProductId(itemDTO.getProductId());
            item.setQuantity(itemDTO.getQuantity());
            item.setPrice(itemDTO.getPrice());
            item.setOrder(order);
            order.getOrderItems().add(item);
        }

        // Recalculate total amount
        BigDecimal totalAmount = order.getOrderItems().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalAmount(totalAmount);

        Order updatedOrder = orderRepository.save(order);
        return mapToResponseDTO(updatedOrder);
    }

   /*
   mapping to DTO
    */
    private OrderResponseDTO mapToResponseDTO(Order order) {
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(order.getId());
        dto.setCustomerId(order.getCustomerId());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setStatus(order.getStatus());

        List<OrderItemDTO> itemDTOs = order.getOrderItems().stream().map(item -> {
            OrderItemDTO itemDTO = new OrderItemDTO();
            itemDTO.setProductId(item.getProductId());
            itemDTO.setQuantity(item.getQuantity());
            itemDTO.setPrice(item.getPrice());
            return itemDTO;
        }).collect(Collectors.toList());

        dto.setOrderItems(itemDTOs);

        return dto;
    }


    /*
    retry payment
     */
    @Override
    public OrderResponseDTO retryPayment(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with ID: " + orderId));

        if (!OrderStatus.PAYMENT_FAILED.equals(order.getStatus())) {
            throw new IllegalStateException("Only orders with PAYMENT_FAILED status can retry payment.");
        }

        PaymentRequestDTO paymentRequest = new PaymentRequestDTO();
        paymentRequest.setOrderId(order.getId());
        paymentRequest.setAmount(order.getTotalAmount());

        PaymentResponseDTO paymentResponse = paymentHelper.processPayment(paymentRequest);

        // Log and update based on response
        System.out.println("Retry Payment Status: " + paymentResponse.getStatus());

        if ("SUCCESS".equalsIgnoreCase(paymentResponse.getStatus())) {
            order.setStatus(OrderStatus.PAYMENT_COMPLETED);
        } else {
            order.setStatus(OrderStatus.PAYMENT_FAILED);
        }

        Order updated = orderRepository.save(order);
        return mapToResponseDTO(updated);
    }


}
