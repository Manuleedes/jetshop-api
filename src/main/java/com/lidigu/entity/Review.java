package com.lidigu.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @Column(name = "review_id", length = 36)
    private String reviewId;


    @Column(name = "review_product_id")
    private String productId;

    @Column(name = "review_user_id")
    private Long userId;

    @Column(name = "review_rating")
    private Double rating;

    @Column(name = "review_title")
    private String title;

    @Column(name = "review_comment", columnDefinition = "TEXT")
    private String comment;

    @Column(name = "review_created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}
