package com.lidigu.repository;

import com.lidigu.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    Page<Product> findByProductIsActive(boolean isActive, Pageable pageable);
    
    @Query("SELECT p FROM Product p WHERE (p.productName LIKE %?1% OR p.productDescription LIKE %?1%) AND (?2 IS NULL OR p.productRating >= ?2)")
    List<Product> searchProducts(String name, Double rating);

    List<Product> findByProductBrandIdAndProductIsActive(Integer brandId, boolean isActive);
    List<Product> findByProductCategoryIdAndProductIsActiveOrderByProductCreatedAtDesc(Integer categoryId, boolean isActive);
}

