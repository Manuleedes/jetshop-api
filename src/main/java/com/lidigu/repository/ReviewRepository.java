package com.lidigu.repository;

import com.lidigu.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, String> {

    List<Review> findByProductId(String productId);
    java.util.Optional<Review> findByProductIdAndUserId(String productId, Long userId);
}

