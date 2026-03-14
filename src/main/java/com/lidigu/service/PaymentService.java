package com.lidigu.service;

import com.lidigu.entity.*;
import com.lidigu.repository.*;
import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderPaymentRepository paymentRepository;
    @Autowired
    private CartItemRepository cartRepository;
    @Autowired
    private PrimeMembershipRepository primeRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @Transactional
    public Map<String, Object> initiatePayment(String order_id, Long user_id, String payment_method,
            BigDecimal amount, String currency) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Order> orderOpt = orderRepository.findById(order_id);
            if (orderOpt.isEmpty()) {
                response.put("status", "error");
                response.put("flag", "invalid_order");
                response.put("message", "Error: Invalid order ID.");
                return response;
            }

            Optional<OrderPayment> paymentOpt = paymentRepository.findByOrderId(order_id);
            if (paymentOpt.isPresent()) {
                response.put("status", "error");
                response.put("flag", "payment_exists");
                response.put("message", "Error: Payment already initiated for this order.");
                return response;
            }

            if ("COD".equalsIgnoreCase(payment_method)) {
                Order order = orderOpt.get();
                order.setPaymentMethod(payment_method);
                order.setPaymentStatus("COD_CONFIRMED");
                orderRepository.save(order);

                cartRepository.deleteByUserId(user_id.toString());

                userRepository.findById(user_id).ifPresent(user -> {
                    try {
                        if (user.getFcmToken() != null) {
                            notificationService.sendPushNotification(user.getFcmToken(), "Order Placed",
                                    "Your order " + order_id + " is placed successfully (COD).");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                response.put("status", "success");
                response.put("flag", "cod_confirmed");
                response.put("message", "Order placed successfully with Cash on Delivery.");
                response.put("payment_status", "COD_CONFIRMED");
            } else {
                OrderPayment payment = new OrderPayment();
                payment.setOrderId(order_id);
                payment.setUserId(user_id);
                payment.setPaymentMethod(payment_method);
                payment.setAmount(amount);
                payment.setCurrency(currency);
                payment.setTransactionStatus("PENDING");
                paymentRepository.save(payment);

                Order order = orderOpt.get();
                order.setPaymentMethod(payment_method);
                order.setPaymentStatus("PENDING");
                orderRepository.save(order);

                response.put("status", "success");
                response.put("flag", "payment_initiated");
                response.put("message", "Payment initiated successfully!");
                response.put("payment_status", "PENDING");
            }
        } catch (Exception e) {
            response.put("status", "error");
            response.put("flag", "initiation_failed");
            response.put("message", e.getMessage());
        }
        return response;
    }

    public Map<String, Object> createStripeIntent(BigDecimal amount, String currency) {
        Map<String, Object> response = new HashMap<>();
        try {
            Stripe.apiKey = stripeSecretKey;

            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(amount.multiply(new BigDecimal("100")).longValue())
                    .setCurrency(currency.toLowerCase())
                    .setAutomaticPaymentMethods(
                            PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                    .setEnabled(true)
                                    .build())
                    .build();

            PaymentIntent intent = PaymentIntent.create(params);

            response.put("status", "success");
            response.put("client_secret", intent.getClientSecret());
            response.put("payment_intent_id", intent.getId());
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
        }
        return response;
    }

    @Transactional
    public Map<String, Object> verifyPayment(String order_id, String transaction_id, String status,
            String gateway_response, Long user_id, String planName,
            String planPrice) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<OrderPayment> paymentOpt = paymentRepository.findByOrderId(order_id);
            if (paymentOpt.isEmpty()) {
                response.put("status", "error");
                response.put("flag", "no_payment_record");
                response.put("message", "Payment record not found.");
                return response;
            }

            OrderPayment payment = paymentOpt.get();
            payment.setTransactionId(transaction_id);
            payment.setTransactionStatus(status);
            payment.setGatewayResponse(gateway_response);
            payment.setUpdatedAt(LocalDateTime.now());
            paymentRepository.save(payment);

            String paymentStatus = "SUCCESS".equalsIgnoreCase(status) ? "PAID" : "FAILED";

            Optional<Order> orderOpt = orderRepository.findById(order_id);
            if (orderOpt.isPresent()) {
                Order order = orderOpt.get();
                order.setPaymentStatus(paymentStatus);
                order.setTransactionStatus(status);
                order.setUpdatedAt(LocalDateTime.now());
                orderRepository.save(order);
            }

            if ("SUCCESS".equalsIgnoreCase(status)) {
                if (planName != null && !planName.isEmpty() && planPrice != null && !"0".equals(planPrice)) {
                    LocalDateTime expiryDate = null;
                    if ("prime".equalsIgnoreCase(planName)) {
                        expiryDate = LocalDateTime.now().plusYears(1);
                    } else if ("primelite".equalsIgnoreCase(planName)) {
                        expiryDate = LocalDateTime.now().plusMonths(6);
                    }

                    if (expiryDate != null) {
                        Optional<PrimeMembership> activePrime = primeRepository.findByUserId(user_id);
                        if (activePrime.isEmpty() || activePrime.get().getExpiryDate().isBefore(LocalDateTime.now())) {
                            PrimeMembership membership = activePrime.orElse(new PrimeMembership());
                            membership.setUserId(user_id);
                            membership.setPlanName(planName);
                            membership.setPlanPrice(new BigDecimal(planPrice));
                            membership.setAmountPaid(new BigDecimal(planPrice));
                            membership.setPaymentMethod(payment.getPaymentMethod());
                            membership.setTransactionId(transaction_id);
                            membership.setTransactionStatus(status);
                            membership.setStartDate(LocalDateTime.now());
                            membership.setExpiryDate(expiryDate);
                            primeRepository.save(membership);
                        }
                    }
                }

                cartRepository.deleteByUserId(user_id.toString());

                userRepository.findById(user_id).ifPresent(user -> {
                    try {
                        if (user.getFcmToken() != null) {
                            notificationService.sendPushNotification(user.getFcmToken(), "Payment Successful",
                                    "Payment for order " + order_id + " was successful!");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } else {
                userRepository.findById(user_id).ifPresent(user -> {
                    try {
                        if (user.getFcmToken() != null) {
                            notificationService.sendPushNotification(user.getFcmToken(), "Payment Failed",
                                    "Payment for order " + order_id + " failed.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }

            response.put("status", "success");
            response.put("flag", "payment_verified");
            response.put("message", "Payment verified successfully");
            response.put("payment_status", paymentStatus);

        } catch (Exception e) {
            response.put("status", "error");
            response.put("flag", "verification_failed");
            response.put("message", e.getMessage());
            response.put("payment_status", "FAILED");
        }
        return response;
    }
}
