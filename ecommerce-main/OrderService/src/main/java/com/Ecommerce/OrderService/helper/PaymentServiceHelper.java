package com.Ecommerce.OrderService.helper;


import com.Ecommerce.OrderService.client.PaymentClient;
import com.Ecommerce.OrderService.dto.PaymentRequestDTO;
import com.Ecommerce.OrderService.dto.PaymentResponseDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentServiceHelper {

    private final PaymentClient paymentClient;

    @CircuitBreaker(name = "paymentService", fallbackMethod = "handlePaymentServiceFallback")
    public PaymentResponseDTO processPayment(PaymentRequestDTO request) {
        return paymentClient.initiatePayment(request);
    }

    public PaymentResponseDTO handlePaymentServiceFallback(PaymentRequestDTO request, Throwable ex) {
        System.out.println("⚠️ Fallback for PaymentService: " + ex.getMessage());
        PaymentResponseDTO fallback = new PaymentResponseDTO();
        fallback.setStatus("FAILED");
        fallback.setTransactionId("N/A");
        fallback.setMessage("Fallback: Payment failed");
        return fallback;
    }
}
