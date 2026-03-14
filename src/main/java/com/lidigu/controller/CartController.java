package com.lidigu.controller;

import com.lidigu.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("get_cart_items")
    public Object getCartItems(@RequestParam String user_id) {
        return cartService.getCartItems(user_id);
    }

    @PostMapping("add_to_cart")
    public Object addToCart(@RequestParam String user_id,
            @RequestParam String product_id,
            @RequestParam String quantity) {
        return cartService.addToCart(user_id, product_id, quantity);
    }

    @PostMapping("remove_from_cart")
    public Object removeFromCart(@RequestParam String user_id,
            @RequestParam String product_id,
            @RequestParam String quantity) {
        return cartService.removeFromCart(user_id, product_id, quantity);
    }

    @PostMapping("delete_cart_item")
    public Object deleteCartItem(@RequestParam String cart_id) {
        return cartService.deleteCartItem(cart_id);
    }
}
