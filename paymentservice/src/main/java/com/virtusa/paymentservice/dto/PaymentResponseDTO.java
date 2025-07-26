package com.virtusa.paymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponseDTO {
    private String paymentId;
    private String orderId;
    private BigDecimal amount;
    private String method;
    private String status;
    private String timestamp;
}