package com.Ecommerce.OrderService.controller;
import com.Ecommerce.OrderService.dto.OrderRequestDTO;
import com.Ecommerce.OrderService.dto.OrderResponseDTO;
import com.Ecommerce.OrderService.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    /**
     * Get all orders
     */
    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        List<OrderResponseDTO> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    /**
     * Place a new order
     */
    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderRequestDTO requestDTO) {
        OrderResponseDTO responseDTO = orderService.createOrder(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    /**
     * Get order details by order ID
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long orderId) {
        OrderResponseDTO responseDTO = orderService.getOrderById(orderId);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Get all orders for a specific customer
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByCustomerId(@PathVariable Long customerId) {
        List<OrderResponseDTO> orders = orderService.getOrdersByCustomerId(customerId);

        if (orders.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(orders);
    }

    /**
     * Cancel an existing order
     */
    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<OrderResponseDTO> cancelOrder(@PathVariable Long orderId) {
        OrderResponseDTO responseDTO = orderService.cancelOrder(orderId);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     *
     * deleting orderby its id
     * @return
     */
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {
        orderService.softDeleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }

/**
 * updating order
 */
@PutMapping("/{orderId}")
public ResponseEntity<OrderResponseDTO> updateOrder(
        @PathVariable Long orderId,
        @Valid @RequestBody OrderRequestDTO requestDTO) {

    OrderResponseDTO updatedOrder = orderService.updateOrder(orderId, requestDTO);
    return ResponseEntity.ok(updatedOrder);
}

/*
   retrying payment option
 */
@PutMapping("/retry-payment/{orderId}")
public ResponseEntity<OrderResponseDTO> retryPayment(@PathVariable Long orderId) {
    OrderResponseDTO response = orderService.retryPayment(orderId);
    return ResponseEntity.ok(response);
}
}
