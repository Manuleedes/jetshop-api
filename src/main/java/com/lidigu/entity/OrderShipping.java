package com.lidigu.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "order_shipping")
public class OrderShipping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_shipping_id")
    private Long orderShippingId;

    @Column(name = "order_id", length = 20)
    private String orderId;

    @Column(name = "user_id")
    private Long userId;

    private String address;

    @Column(name = "google_address")
    private String googleAddress;

    private BigDecimal latitude;
    private BigDecimal longitude;
    private String city;
    private String state;

    @Column(name = "postal_code")
    private String postalCode;

    private String country;
    private String phone;

    @Column(name = "shipping_status")
    private String shippingStatus;
}
