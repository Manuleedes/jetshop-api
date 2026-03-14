package com.lidigu.mapper;

import com.lidigu.dto.ProductDTO;
import com.lidigu.entity.Product;
import com.lidigu.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    @Autowired
    private ReviewMapper reviewMapper;

    @Autowired
    private ReviewRepository reviewRepository;

    public ProductDTO toDto(Product product) {
        if (product == null)
            return null;
        ProductDTO dto = new ProductDTO();
        dto.setProductId(product.getProductId());
        dto.setProductName(product.getProductName());
        dto.setProductDescription(product.getProductDescription());
        dto.setProductPrice(product.getProductPrice());
        dto.setProductDiscountPrice(product.getProductDiscountPrice());
        dto.setProductImage(product.getProductImageUrl());
        dto.setProductCategoryId(product.getProductCategoryId());
        dto.setProductQuantity(product.getProductStockQuantity());
        dto.setProductRating(product.getProductRating() != null ? product.getProductRating().doubleValue() : 0.0);
        dto.setProductTotalReviews(product.getProductTotalReviews());
        dto.setIsFeatured(Boolean.TRUE.equals(product.getProductIsFeatured()) ? 1 : 0);

        // Brand logic could be added here if needed, but currently entity has
        // productBrandId
        dto.setProductBrand("");

        // Map reviews if needed
        dto.setReviews(reviewRepository.findByProductId(product.getProductId())
                .stream()
                .map(reviewMapper::toDto)
                .collect(Collectors.toList()));

        return dto;
    }
}
