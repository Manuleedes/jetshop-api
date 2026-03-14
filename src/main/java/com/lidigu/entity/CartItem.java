package com.lidigu.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "cart")
public class CartItem {
    @Id
    @Column(name = "cart_id", length = 36)
    private String cartId;

    @Column(name = "user_id", length = 36)
    private String userId;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    private Product product;

    private Integer quantity;

    @Column(name = "added_at", insertable = false, updatable = false)
    private LocalDateTime addedAt;
}
