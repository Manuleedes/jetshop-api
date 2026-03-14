package com.lidigu.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "user_addresses")
public class UserAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    private String address;
    private String city;
    private String state;

    @Column(name = "zip_code")
    private String zipCode;

    private String country;
    private Double latitude;
    private Double longitude;
    private String type; // Home, Office

    @Column(name = "default_address")
    private Integer defaultAddress; // 0 or 1

    @Column(name = "google_address")
    private String googleAddress;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}
