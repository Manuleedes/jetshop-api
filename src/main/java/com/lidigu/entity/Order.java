package com.lidigu.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @Column(name = "order_id", length = 20)
    private String orderId;

    @Column(name = "user_id")
    private Long userId;

    private BigDecimal subtotal;

    @Column(name = "discount_value")
    private BigDecimal discountValue;

    @Column(name = "coupon_code")
    private String couponCode;

    @Column(name = "tax_amount")
    private BigDecimal taxAmount;

    @Column(name = "delivery_charges")
    private BigDecimal deliveryCharges;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    private String status;

    @Column(name = "transaction_status")
    private String transactionStatus;

    @Column(name = "payment_status")
    private String paymentStatus;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}
