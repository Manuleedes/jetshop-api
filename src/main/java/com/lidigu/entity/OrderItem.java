package com.lidigu.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_details_id")
    private Long orderDetailsId;

    @Column(name = "order_id", length = 20)
    private String orderId;

    @Column(name = "product_id")
    private String productId;

    private Integer quantity;
    private BigDecimal price;
    private BigDecimal tax;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @Column(name = "item_status")
    private String itemStatus;
}
