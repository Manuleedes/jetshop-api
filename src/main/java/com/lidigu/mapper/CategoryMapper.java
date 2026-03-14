package com.lidigu.mapper;

import com.lidigu.dto.CategoryDTO;
import com.lidigu.entity.ProductCategory;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryDTO toDto(ProductCategory category) {
        if (category == null)
            return null;
        CategoryDTO dto = new CategoryDTO();
        dto.setCategoryId(category.getId());
        dto.setCategoryName(category.getName());
        dto.setCategoryImage(category.getIcon());
        return dto;
    }
}
