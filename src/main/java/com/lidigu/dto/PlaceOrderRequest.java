package com.lidigu.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class PlaceOrderRequest {
    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("Subtotal")
    private BigDecimal subtotal;

    @JsonProperty("Discount")
    private BigDecimal discount;

    @JsonProperty("Tax")
    private BigDecimal tax;

    @JsonProperty("Delivery Charges")
    private BigDecimal deliveryCharges;

    @JsonProperty("Final Total")
    private BigDecimal finalTotal;

    @JsonProperty("payment_method")
    private String paymentMethod;

    @JsonProperty("cart_items")
    private List<CartItemRequest> cartItems;

    @JsonProperty("shipping_address_id")
    private Long shippingAddressId;

    @Data
    public static class CartItemRequest {
        @JsonProperty("product_id")
        private String productId;

        @JsonProperty("quantity")
        private Integer quantity;

        @JsonProperty("quantity_amount_price")
        private BigDecimal quantityAmountPrice;

        @JsonProperty("tax_amount")
        private BigDecimal taxAmount;

        @JsonProperty("quantity_amount_price_with_tax")
        private BigDecimal quantityAmountPriceWithTax;
    }
}
