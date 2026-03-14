package com.lidigu.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "coupons")
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long couponId;

    @Column(name = "coupon_code", unique = true, nullable = false)
    private String couponCode;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "discount_value")
    private BigDecimal discountValue;

    @Column(name = "discount_type")
    private String discountType; // fixed, percentage

    @Column(name = "min_order_amount")
    private BigDecimal minOrderAmount;

    @Column(name = "max_discount")
    private BigDecimal maxDiscount;

    @Column(name = "usage_limit")
    private Integer usageLimit;

    @Column(name = "used_count")
    private Integer usedCount;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    private String status; // active, expired, disabled
}
