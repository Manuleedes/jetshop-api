package com.lidigu.mapper;

import com.lidigu.dto.CartItemDTO;
import com.lidigu.entity.CartItem;
import org.springframework.stereotype.Component;

@Component
public class CartMapper {

    public CartItemDTO toDto(CartItem item) {
        if (item == null)
            return null;
        CartItemDTO dto = new CartItemDTO();
        dto.setCartItemId(item.getCartId());

        dto.setProductId(item.getProduct().getProductId());
        dto.setProductName(item.getProduct().getProductName());
        dto.setProductImage(item.getProduct().getProductImageUrl());
        dto.setQuantity(item.getQuantity());

        // Price might need to come from product if not in cart item
        java.math.BigDecimal price = item.getProduct().getProductDiscountPrice() != null
                ? item.getProduct().getProductDiscountPrice()
                : item.getProduct().getProductPrice();

        dto.setPrice(price);
        if (dto.getPrice() != null && dto.getQuantity() != null) {
            dto.setTotalPrice(dto.getPrice().multiply(new java.math.BigDecimal(dto.getQuantity())));
        }
        return dto;
    }
}
