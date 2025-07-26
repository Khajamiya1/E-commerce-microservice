package com.Ecommerce.OrderService.dto;

import com.Ecommerce.OrderService.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class OrderResponseDTO {

    private Long id;
    private Long customerId;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private List<OrderItemDTO> orderItems;

    public void setOrderId(long l) {
    }
}
