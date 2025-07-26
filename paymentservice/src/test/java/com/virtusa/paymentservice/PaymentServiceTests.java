package com.virtusa.paymentservice;

import com.virtusa.paymentservice.dto.PaymentRequestDTO;
import com.virtusa.paymentservice.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class PaymentServiceTests {

    @Autowired
    private PaymentService service;

    @Test
    void testInitiatePayment() {
        PaymentRequestDTO dto = new PaymentRequestDTO("order123", new BigDecimal("100.00"), "credit_card");
        var response = service.initiatePayment(dto);
        assertNotNull(response.getPaymentId());
        assertEquals("initiated", response.getStatus());
    }
}