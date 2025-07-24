package com.Ecommerce.OrderService.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderRequestDTO {

    @NotNull
    private Long customerId;

    @NotNull
    private List<OrderItemDTO> orderItems;
}
