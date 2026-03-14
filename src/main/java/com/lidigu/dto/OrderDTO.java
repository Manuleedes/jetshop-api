package com.lidigu.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderDTO {
    @JsonProperty("order_id")
    private String orderId;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("order_number")
    private String orderNumber;

    @JsonProperty("total_amount")
    private BigDecimal totalAmount;

    @JsonProperty("order_status")
    private String orderStatus;

    @JsonProperty("payment_status")
    private String paymentStatus;

    @JsonProperty("payment_method")
    private String paymentMethod;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("items")
    private List<OrderItemDTO> items;
}
