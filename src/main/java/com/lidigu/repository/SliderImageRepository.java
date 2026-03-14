package com.lidigu.repository;

import com.lidigu.entity.SliderImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SliderImageRepository extends JpaRepository<SliderImage, Long> {
    List<SliderImage> findByIsActiveTrueOrderByCreatedAtDesc();
}
