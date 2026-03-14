package com.lidigu.repository;

import com.lidigu.entity.CartItem;
import com.lidigu.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, String> {
    Optional<CartItem> findByUserIdAndProduct(String userId, Product product);
    List<CartItem> findByUserId(String userId);
    void deleteByUserId(String userId);
}

