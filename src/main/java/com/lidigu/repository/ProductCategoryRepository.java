package com.lidigu.repository;

import com.lidigu.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
    List<ProductCategory> findByDisableFlagOrderByPriorityAsc(Integer disableFlag);
    List<ProductCategory> findByDisableFlag(Integer disableFlag);
}
