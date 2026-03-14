package com.lidigu.service;

import com.lidigu.entity.Product;
import com.lidigu.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SearchService {

    @Autowired
    private ProductRepository productRepository;

    public Map<String, Object> search(String productName, Double rating) {
        Map<String, Object> response = new HashMap<>();

        List<Product> products = productRepository.searchProducts(productName, rating);

        if (products.isEmpty()) {
            response.put("status", "error");
            response.put("flag", "no_products");
            response.put("message", "No products found");
        } else {
            response.put("status", "success");
            response.put("flag", "products_found");
            response.put("data", products.stream()
                    .map(this::mapProductToData)
                    .collect(Collectors.toList()));
        }
        return response;
    }

    private Map<String, Object> mapProductToData(Product p) {
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
        return map;
    }
}
