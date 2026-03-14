package com.lidigu.repository;

import com.lidigu.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
    Optional<Coupon> findByCouponCodeAndStatus(String couponCode, String status);

    @Query("SELECT c FROM Coupon c WHERE c.status = 'active' AND c.expiryDate >= CURDATE()")
    java.util.List<Coupon> findActiveCoupons();
}

