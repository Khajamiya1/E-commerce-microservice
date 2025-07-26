package com.Ecommerce.OrderService.client;

import com.Ecommerce.OrderService.dto.PaymentRequestDTO;
import com.Ecommerce.OrderService.dto.PaymentResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "paymentservice")
public interface PaymentClient {

    @PostMapping("/api/payments")
    PaymentResponseDTO initiatePayment(@RequestBody PaymentRequestDTO request);
}