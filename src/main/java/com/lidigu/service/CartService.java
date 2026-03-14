package com.lidigu.service;

import com.lidigu.common.ApiResponse;
import com.lidigu.dto.CartItemDTO;
import com.lidigu.entity.CartItem;
import com.lidigu.entity.Product;
import com.lidigu.mapper.CartMapper;
import com.lidigu.repository.CartItemRepository;
import com.lidigu.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private CartItemRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartMapper cartMapper;

    public Object getCartItems(String user_id) {
        try {
            List<CartItem> cartItems = cartRepository.findByUserId(user_id);
            if (cartItems.isEmpty()) {
                return ApiResponse.phpError("No items in cart");
            }

            List<CartItemDTO> dtoList = cartItems.stream()
                    .map(cartMapper::toDto)
                    .collect(Collectors.toList());

            BigDecimal totalCartPrice = dtoList.stream()
                    .map(CartItemDTO::getTotalPrice)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            return ApiResponse.success("Cart items retrieved.", dtoList);
        } catch (Exception e) {
            return ApiResponse.error("Error: " + e.getMessage());
        }
    }

    public Object addToCart(String user_id, String product_id, String quantity) {
        int qty = Integer.parseInt(quantity);

        Optional<Product> productOpt = productRepository.findById(product_id);
        if (productOpt.isEmpty()) {
            return ApiResponse.error("Product not found");
        }

        Product product = productOpt.get();
        if (product.getProductStockQuantity() < qty) {
            return ApiResponse.phpError("Not enough stock available");
        }

        Optional<CartItem> existingItemOpt = cartRepository.findByUserIdAndProduct(user_id, product);
        if (existingItemOpt.isPresent()) {
            CartItem existingItem = existingItemOpt.get();
            int newQuantity = existingItem.getQuantity() + qty;
            if (newQuantity > product.getProductStockQuantity()) {
                return ApiResponse.error("Not enough stock available");
            }
            existingItem.setQuantity(newQuantity);
            cartRepository.save(existingItem);
            return ApiResponse.success("Cart updated", null);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCartId("CART" + System.currentTimeMillis());
            newItem.setUserId(user_id);
            newItem.setProduct(product);
            newItem.setQuantity(qty);
            cartRepository.save(newItem);
            return ApiResponse.success("Added to cart", null);
        }
    }

    public Object removeFromCart(String user_id, String product_id, String quantity) {
        int qty = Integer.parseInt(quantity);

        Optional<Product> productOpt = productRepository.findById(product_id);
        if (productOpt.isEmpty()) {
            return ApiResponse.error("Product not found");
        }

        Optional<CartItem> cartItemOpt = cartRepository.findByUserIdAndProduct(user_id, productOpt.get());
        if (cartItemOpt.isPresent()) {
            CartItem cartItem = cartItemOpt.get();
            if (cartItem.getQuantity() > qty) {
                cartItem.setQuantity(cartItem.getQuantity() - qty);
                cartRepository.save(cartItem);
                return ApiResponse.success("Cart updated", null);
            } else {
                cartRepository.delete(cartItem);
                return ApiResponse.success("Product removed from cart", null);
            }
        } else {
            return ApiResponse.phpError("Product not found in cart");
        }
    }

    public Object deleteCartItem(String cart_id) {
        Optional<CartItem> cartItemOpt = cartRepository.findById(cart_id);
        if (cartItemOpt.isPresent()) {
            cartRepository.delete(cartItemOpt.get());
            return ApiResponse.success("Cart item deleted successfully", null);
        } else {
            return ApiResponse.phpError("Cart item not found");
        }
    }
}
