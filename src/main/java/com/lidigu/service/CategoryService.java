package com.lidigu.service;

import com.lidigu.common.ApiResponse;
import com.lidigu.dto.CategoryDTO;
import com.lidigu.entity.ProductCategory;
import com.lidigu.mapper.CategoryMapper;
import com.lidigu.repository.ProductCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private ProductCategoryRepository categoryRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    @Value("${app.base-url}")
    private String iconBaseUrl;

    public Object getAllCategories() {
        try {
            List<ProductCategory> categories = categoryRepository.findByDisableFlagOrderByPriorityAsc(0);

            List<CategoryDTO> categoryDTOs = categories.stream()
                    .map(c -> {
                        if (c.getIcon() != null && !c.getIcon().startsWith("http")) {
                            c.setIcon(iconBaseUrl + c.getIcon());
                        }
                        return categoryMapper.toDto(c);
                    })
                    .collect(Collectors.toList());

            return ApiResponse.success("categories", categoryDTOs);
        } catch (Exception e) {
            return ApiResponse.error("Failed to fetch categories: " + e.getMessage());
        }
    }
}
