package com.lidigu.controller;

import com.lidigu.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("initiate_payment")
    public Map<String, Object> initiatePayment(@RequestParam String order_id,
            @RequestParam Long user_id,
            @RequestParam String payment_method,
            @RequestParam BigDecimal amount,
            @RequestParam String currency) {
        return paymentService.initiatePayment(order_id, user_id, payment_method, amount, currency);
    }

    @PostMapping("create-stripe-intent")
    public Map<String, Object> createStripeIntent(@RequestParam BigDecimal amount, @RequestParam String currency) {
        return paymentService.createStripeIntent(amount, currency);
    }

    @PostMapping("verify_payment")
    public Map<String, Object> verifyPayment(@RequestParam String order_id,
            @RequestParam String transaction_id,
            @RequestParam String status,
            @RequestParam String gateway_response,
            @RequestParam Long user_id,
            @RequestParam(required = false) String planName,
            @RequestParam(required = false) String planPrice) {
        return paymentService.verifyPayment(order_id, transaction_id, status, gateway_response, user_id, planName,
                planPrice);
    }
}
