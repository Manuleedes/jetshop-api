package com.lidigu.controller;

import com.lidigu.dto.PlaceOrderRequest;
import com.lidigu.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("place_order.php")
    public Object placeOrder(@RequestBody PlaceOrderRequest data) {
        return orderService.placeOrder(data);
    }

    @PostMapping("getUserOrder.php")
    public Object getUserOrders(@RequestParam Long user_id) {
        return orderService.getUserOrders(user_id);
    }

    @PostMapping("cancel_order.php")
    public Object cancelOrder(@RequestParam Long user_id,
            @RequestParam String order_id) {
        return orderService.cancelOrder(user_id, order_id);
    }

    @PostMapping("GetOrderDetails.php")
    public Object getOrderDetails(@RequestParam String user_id,
            @RequestParam(required = false) String coupon_code) {
        return orderService.getOrderDetails(user_id, coupon_code);
    }

    @PostMapping("get_placeorder_details.php")
    public Object getPlaceOrderDetails(@RequestParam String order_id) {
        return orderService.getPlaceOrderDetails(order_id);
    }
}
