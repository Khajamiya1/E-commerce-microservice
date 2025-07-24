package com.Ecommerce.OrderService.dto;
import lombok.Data;

@Data
public class PaymentResponseDTO {
    private  Long orderId;
    private String status;
    private String transactionId;
    private String message;
}