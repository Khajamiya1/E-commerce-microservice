package com.virtusa.paymentservice.service;

import com.virtusa.paymentservice.dto.PaymentRequestDTO;
import com.virtusa.paymentservice.dto.PaymentResponseDTO;
import com.virtusa.paymentservice.model.Payment;
import com.virtusa.paymentservice.model.PaymentMethod;
import com.virtusa.paymentservice.model.PaymentStatus;
import com.virtusa.paymentservice.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository repository;

    @Transactional
    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public PaymentResponseDTO initiatePayment(PaymentRequestDTO request) {
        Payment payment;
        payment = Payment.builder()
                .id(UUID.randomUUID().toString())
                .orderId(request.getOrderId())
                .amount(request.getAmount())
                .method(PaymentMethod.valueOf(request.getMethod()))
                .status(PaymentStatus.initiated)
                .timestamp(LocalDateTime.now())
                .build();
        System.out.println("uuid"+payment.getId());
        Payment saved = repository.save(payment);
        return mapToResponse(saved);
    }

    public PaymentResponseDTO getPaymentStatus(String id) {
        return repository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
    }

    @Transactional
    public PaymentResponseDTO updatePaymentStatus(String id, String status) {
        Payment payment = repository.findById(id).orElseThrow(() -> new RuntimeException("Payment not found"));
        payment.setStatus(PaymentStatus.valueOf(status));
        return mapToResponse(repository.save(payment));
    }

    public List<PaymentResponseDTO> getPaymentsByOrderId(String orderId) {
        return repository.findByOrderId(orderId).stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    private PaymentResponseDTO mapToResponse(Payment p) {
        return PaymentResponseDTO.builder()
                .paymentId(p.getId())
                .orderId(p.getOrderId())
                .amount(p.getAmount())
                .method(p.getMethod().name())
                .status(p.getStatus().name())
                .timestamp(p.getTimestamp().toString())
                .build();
    }
}