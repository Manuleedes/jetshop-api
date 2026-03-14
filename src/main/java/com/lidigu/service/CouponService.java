package com.lidigu.service;

import com.lidigu.entity.Coupon;
import com.lidigu.entity.Order;
import com.lidigu.repository.CouponRepository;
import com.lidigu.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Transactional
    public Map<String, Object> applyCoupon(String order_id, String coupon_code) {
        Map<String, Object> response = new HashMap<>();

        Optional<Order> orderOpt = orderRepository.findById(order_id);
        if (orderOpt.isEmpty()) {
            response.put("status", "error");
            response.put("flag", "invalid_order");
            response.put("message", "Invalid order ID");
            response.put("discount", "0");
            response.put("new_total", 0.0);
            return response;
        }

        Order order = orderOpt.get();
        if (order.getCouponCode() != null && !order.getCouponCode().isEmpty()) {
            response.put("status", "error");
            response.put("flag", "already_applied");
            response.put("message", "A coupon is already applied to this order");
            response.put("discount", "0");
            response.put("new_total", order.getTotalAmount() != null ? order.getTotalAmount().doubleValue() : 0.0);
            return response;
        }

        Optional<Coupon> couponOpt = couponRepository.findByCouponCodeAndStatus(coupon_code, "active");
        if (couponOpt.isEmpty()) {
            response.put("status", "error");
            response.put("flag", "invalid_coupon");
            response.put("message", "Invalid or expired coupon");
            response.put("discount", "0");
            response.put("new_total", order.getTotalAmount() != null ? order.getTotalAmount().doubleValue() : 0.0);
            return response;
        }

        Coupon coupon = couponOpt.get();
        if (coupon.getExpiryDate().isBefore(LocalDate.now())) {
            response.put("status", "error");
            response.put("flag", "expired_coupon");
            response.put("message", "Invalid or expired coupon");
            response.put("discount", "0");
            response.put("new_total", order.getTotalAmount().doubleValue());
            return response;
        }

        if (coupon.getUsedCount() >= coupon.getUsageLimit()) {
            response.put("status", "error");
            response.put("flag", "limit_reached");
            response.put("message", "Coupon usage limit reached");
            response.put("discount", "0");
            response.put("new_total", order.getTotalAmount().doubleValue());
            return response;
        }

        if (order.getSubtotal().compareTo(coupon.getMinOrderAmount()) < 0) {
            response.put("status", "error");
            response.put("flag", "min_amount_not_met");
            response.put("message", "Order total is less than the required minimum");
            response.put("discount", "0");
            response.put("new_total", order.getTotalAmount().doubleValue());
            return response;
        }

        BigDecimal discount = BigDecimal.ZERO;
        if ("fixed".equalsIgnoreCase(coupon.getDiscountType())) {
            discount = coupon.getDiscountValue();
        } else { // percentage
            discount = order.getSubtotal().multiply(coupon.getDiscountValue().divide(new BigDecimal("100")));
            if (coupon.getMaxDiscount() != null) {
                discount = discount.min(coupon.getMaxDiscount());
            }
        }

        discount = discount.min(order.getSubtotal());
        BigDecimal newTotal = order.getTotalAmount().subtract(discount);

        order.setDiscountValue(discount);
        order.setCouponCode(coupon_code);
        order.setTotalAmount(newTotal);
        orderRepository.save(order);

        coupon.setUsedCount(coupon.getUsedCount() + 1);
        couponRepository.save(coupon);

        response.put("status", "success");
        response.put("flag", "coupon_applied");
        response.put("message", "Coupon applied successfully");
        response.put("discount", discount.setScale(2, RoundingMode.HALF_UP).toString());
        response.put("new_total", newTotal.setScale(2, RoundingMode.HALF_UP).doubleValue());

        return response;
    }

    @Transactional
    public Map<String, Object> removeCoupon(String order_id, String coupon_code) {
        Map<String, Object> response = new HashMap<>();

        Optional<Order> orderOpt = orderRepository.findById(order_id);
        if (orderOpt.isEmpty()) {
            response.put("status", "error");
            response.put("flag", "invalid_order");
            response.put("message", "Order ID is required.");
            response.put("discount", "0");
            response.put("new_total", 0.0);
            return response;
        }

        Order order = orderOpt.get();
        if (order.getCouponCode() == null || order.getCouponCode().isEmpty()) {
            response.put("status", "error");
            response.put("flag", "no_coupon");
            response.put("message", "No applied coupon found.");
            response.put("discount", "0");
            response.put("new_total", order.getTotalAmount() != null ? order.getTotalAmount().doubleValue() : 0.0);
            return response;
        }

        String actualCouponCode = order.getCouponCode();
        Optional<Coupon> couponOpt = couponRepository.findByCouponCodeAndStatus(actualCouponCode, "active");
        if (couponOpt.isPresent()) {
            Coupon coupon = couponOpt.get();
            coupon.setUsedCount(Math.max(0, coupon.getUsedCount() - 1));
            couponRepository.save(coupon);
        }

        BigDecimal restoredTotal = order.getSubtotal()
                .add(order.getTaxAmount() != null ? order.getTaxAmount() : BigDecimal.ZERO)
                .add(order.getDeliveryCharges() != null ? order.getDeliveryCharges() : BigDecimal.ZERO);

        order.setCouponCode(null);
        order.setDiscountValue(BigDecimal.ZERO);
        order.setTotalAmount(restoredTotal);
        orderRepository.save(order);

        response.put("status", "success");
        response.put("flag", "coupon_removed");
        response.put("message", "Coupon removed successfully.");
        response.put("discount", "0");
        response.put("new_total", restoredTotal.setScale(2, RoundingMode.HALF_UP).doubleValue());

        return response;
    }

    public Map<String, Object> getActiveCoupons() {
        Map<String, Object> response = new HashMap<>();
        List<Coupon> coupons = couponRepository.findActiveCoupons();
        if (coupons.isEmpty()) {
            response.put("status", "error");
            response.put("flag", "no_coupons");
            response.put("message", "No active coupons found");
        } else {
            response.put("status", "success");
            response.put("flag", "coupons_retrieved");
            response.put("message", "Coupons fetched successfully");
            response.put("data", coupons);
        }
        return response;
    }

    @Transactional
    public Map<String, Object> updateCouponUsage(String coupon_code) {
        Map<String, Object> response = new HashMap<>();
        Optional<Coupon> couponOpt = couponRepository.findByCouponCodeAndStatus(coupon_code, "active");

        if (couponOpt.isPresent()) {
            Coupon coupon = couponOpt.get();
            if (coupon.getExpiryDate().isAfter(LocalDate.now().minusDays(1))) {
                coupon.setUsedCount(coupon.getUsedCount() + 1);
                couponRepository.save(coupon);
                response.put("status", "success");
                response.put("flag", "usage_updated");
                response.put("message", "Coupon usage updated");
            } else {
                response.put("status", "error");
                response.put("flag", "expired");
                response.put("message", "Coupon expired");
            }
        } else {
            response.put("status", "error");
            response.put("flag", "invalid_coupon");
            response.put("message", "Invalid or inactive coupon");
        }
        return response;
    }
}
