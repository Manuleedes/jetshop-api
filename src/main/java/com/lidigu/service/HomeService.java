package com.lidigu.service;

import com.lidigu.entity.*;
import com.lidigu.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HomeService {

    @Autowired
    private SliderImageRepository sliderImageRepository;

    @Autowired
    private ProductCategoryRepository categoryRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    public Map<String, Object> getHomeData(String userId) {
        Map<String, Object> response = new HashMap<>();

        List<SliderImage> sliders = sliderImageRepository.findByIsActiveTrueOrderByCreatedAtDesc();
        List<ProductCategory> categories = categoryRepository.findByDisableFlag(0);
        List<Brand> brands = brandRepository.findByStatusOrderByBrandNameAsc("active");

        List<Section> rawSections = sectionRepository.findAll();
        List<Map<String, Object>> sections = rawSections.stream().map(section -> {
            Map<String, Object> sectionMap = new HashMap<>();
            sectionMap.put("section_id", section.getSectionId());
            sectionMap.put("section_name", section.getSectionName());

            List<Map<String, Object>> products = section.getProducts().stream()
                    .map(product -> mapProductToData(product, userId))
                    .collect(Collectors.toList());

            sectionMap.put("products", products);
            return sectionMap;
        }).collect(Collectors.toList());

        response.put("status", "success");
        response.put("sections", sections);
        response.put("slider_images", sliders);
        response.put("categories", categories);
        response.put("brands", brands);

        return response;
    }

    private Map<String, Object> mapProductToData(Product p, String userId) {
        Map<String, Object> map = new HashMap<>();
        map.put("product_id", p.getProductId());
        map.put("product_name", p.getProductName());
        map.put("product_description", p.getProductDescription());
        map.put("product_price", p.getProductPrice() != null ? p.getProductPrice().toString() : "0");
        map.put("product_discount_price",
                p.getProductDiscountPrice() != null ? p.getProductDiscountPrice().toString() : "0");
        map.put("product_stock_quantity", p.getProductStockQuantity());
        map.put("product_category_id", p.getProductCategoryId());
        map.put("product_brand_id", p.getProductBrandId());
        map.put("product_weight", p.getProductWeight());
        map.put("product_dimensions", p.getProductDimensions());
        map.put("product_color", p.getProductColor());
        map.put("product_size", p.getProductSize());
        map.put("product_rating", p.getProductRating() != null ? p.getProductRating().toString() : "0");
        map.put("product_total_reviews", p.getProductTotalReviews());
        map.put("product_image_url", p.getProductImageUrl());
        map.put("product_thumbnail_url", p.getProductThumbnailUrl());
        map.put("product_is_featured", p.getProductIsFeatured() ? 1 : 0);
        map.put("product_is_active", p.getProductIsActive() ? 1 : 0);
        map.put("product_created_at", p.getProductCreatedAt() != null ? p.getProductCreatedAt().toString() : "");
        map.put("product_updated_at", p.getProductUpdatedAt() != null ? p.getProductUpdatedAt().toString() : "");

        map.put("user_cart_quantity", "0");
        map.put("cart_id", "");
        map.put("quantity", "0");

        if (userId != null) {
            cartItemRepository.findByUserIdAndProduct(userId, p).ifPresent(item -> {
                map.put("user_cart_quantity", String.valueOf(item.getQuantity()));
                map.put("cart_id", item.getCartId());
                map.put("quantity", String.valueOf(item.getQuantity()));
            });
        }

        return map;
    }
}
