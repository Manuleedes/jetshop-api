package com.lidigu.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "products")
public class Product {
    @Id
    @Column(name = "product_id")
    private String productId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_description", columnDefinition = "TEXT")
    private String productDescription;

    @Column(name = "product_price")
    private BigDecimal productPrice;

    @Column(name = "product_discount_price")
    private BigDecimal productDiscountPrice;

    @Column(name = "product_stock_quantity")
    private Integer productStockQuantity;

    @Column(name = "product_category_id")
    private Integer productCategoryId;

    @Column(name = "product_brand_id")
    private Integer productBrandId;

    @Column(name = "product_weight")
    private BigDecimal productWeight;

    @Column(name = "product_dimensions")
    private String productDimensions;

    @Column(name = "product_color")
    private String productColor;

    @Column(name = "product_size")
    private String productSize;

    @Column(name = "product_rating")
    private BigDecimal productRating;

    @Column(name = "product_total_reviews")
    private Integer productTotalReviews;

    @Column(name = "product_image_url", columnDefinition = "TEXT")
    private String productImageUrl;

    @Column(name = "product_thumbnail_url", columnDefinition = "TEXT")
    private String productThumbnailUrl;

    @Column(name = "product_is_featured")
    private Boolean productIsFeatured;

    @Column(name = "product_is_active")
    private Boolean productIsActive;

    @Column(name = "product_created_at", insertable = false, updatable = false)
    private LocalDateTime productCreatedAt;

    @Column(name = "product_updated_at", insertable = false, updatable = false)
    private LocalDateTime productUpdatedAt;
}
