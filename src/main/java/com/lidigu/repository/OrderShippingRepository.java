package com.lidigu.repository;

import com.lidigu.entity.OrderShipping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface OrderShippingRepository extends JpaRepository<OrderShipping, Long> {
    Optional<OrderShipping> findByOrderId(String orderId);
}
