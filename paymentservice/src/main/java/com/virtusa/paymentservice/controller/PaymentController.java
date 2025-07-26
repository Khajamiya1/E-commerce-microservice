package com.virtusa.paymentservice.controller;

import com.virtusa.paymentservice.dto.PaymentRequestDTO;
import com.virtusa.paymentservice.dto.PaymentResponseDTO;
import com.virtusa.paymentservice.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService service;

    @PostMapping
    public ResponseEntity<PaymentResponseDTO> initiatePayment(@Valid @RequestBody PaymentRequestDTO dto) {
        return new ResponseEntity<>(service.initiatePayment(dto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponseDTO> getStatus(@PathVariable String id) {
        return ResponseEntity.ok(service.getPaymentStatus(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentResponseDTO> updateStatus(@PathVariable String id, @RequestParam String status) {
        return ResponseEntity.ok(service.updatePaymentStatus(id, status));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<PaymentResponseDTO>> listByOrder(@PathVariable String orderId) {
        return ResponseEntity.ok(service.getPaymentsByOrderId(orderId));
    }
}