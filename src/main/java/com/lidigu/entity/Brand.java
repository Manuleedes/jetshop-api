package com.lidigu.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "brands")
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brand_id")
    private Long brandId;

    @Column(name = "brand_name", unique = true, nullable = false)
    private String brandName;

    @Column(name = "brand_logo")
    private String brandLogo;

    @Column(name = "brand_description", columnDefinition = "TEXT")
    private String brandDescription;

    @Column(name = "website_url")
    private String websiteUrl;

    @Column(name = "status")
    private String status; // active, inactive

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
