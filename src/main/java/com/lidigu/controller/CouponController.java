package com.lidigu.controller;

import com.lidigu.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/")
public class CouponController {

    @Autowired
    private CouponService couponService;

    @PostMapping("apply_coupon")
    public Map<String, Object> applyCoupon(@RequestParam String order_id,
            @RequestParam String coupon_code) {
        return couponService.applyCoupon(order_id, coupon_code);
    }

    @PostMapping("remove_coupon")
    public Map<String, Object> removeCoupon(@RequestParam String order_id,
            @RequestParam(required = false) String coupon_code) {
        return couponService.removeCoupon(order_id, coupon_code);
    }

    @GetMapping("getCouponCode")
    public Map<String, Object> getActiveCoupons() {
        return couponService.getActiveCoupons();
    }

    @PostMapping("update_coupon_usage")
    public Map<String, Object> updateCouponUsage(@RequestParam String coupon_code) {
        return couponService.updateCouponUsage(coupon_code);
    }
}
