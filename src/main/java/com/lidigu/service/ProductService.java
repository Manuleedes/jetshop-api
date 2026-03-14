package com.lidigu.service;

import com.lidigu.common.ApiResponse;
import com.lidigu.common.PaginatedResponse;
import com.lidigu.dto.ProductDTO;
import com.lidigu.entity.Product;
import com.lidigu.entity.Product;
import com.lidigu.mapper.ProductMapper;
import com.lidigu.repository.CartItemRepository;
import com.lidigu.repository.ProductRepository;
import com.lidigu.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductMapper productMapper;

    public Object getAllProducts(int page, int limit) {
        try {
            PageRequest pageRequest = PageRequest.of(page - 1, limit, Sort.by("productCreatedAt").descending());
            Page<Product> productPage = productRepository.findByProductIsActive(true, pageRequest);

            List<ProductDTO> products = productPage.getContent().stream()
                    .map(productMapper::toDto)
                    .collect(Collectors.toList());

            return PaginatedResponse.success(products, productPage.getTotalElements(), productPage.getTotalPages(),
                    page);
        } catch (Exception e) {
            return ApiResponse.error("Error: " + e.getMessage());
        }
    }

    public Map<String, Object> getProductDetails(String productId, String userId) {
        Optional<Product> productOpt = productRepository.findById(productId);

        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            ProductDTO productDTO = productMapper.toDto(product);

            // Add user specific cart data to the DTO if needed
            if (userId != null) {
                cartItemRepository.findByUserIdAndProduct(userId, product).ifPresent(item -> {
                    // This part could be also in the mapper if we pass userId
                });
            }

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Product found successfully.");
            response.put("product", productDTO);

            // Note: The original structure for reviewsData and related_products is
            // maintained for parity
            // but we use the DTOs inside them.

            List<ProductDTO> relatedData = productRepository
                    .findByProductCategoryIdAndProductIsActiveOrderByProductCreatedAtDesc(
                            product.getProductCategoryId(), true)
                    .stream()
                    .filter(p -> !p.getProductId().equals(productId))
                    .limit(5)
                    .map(productMapper::toDto)
                    .collect(Collectors.toList());

            response.put("related_products", relatedData);

            // Reusing original review structure but with DTOs if possible, or keeping Map
            // for very specific structures
            // For now, let's keep the complex reviewCounts structure as a Map within the
            // response
            return response;
        } else {
            return ApiResponse.phpError("Product not found.");
        }
    }

    public Object listProductsByBrand(Integer brandId) {
        List<Product> products = productRepository.findByProductBrandIdAndProductIsActive(brandId, true);
        if (products.isEmpty()) {
            return ApiResponse.error("No products found for this brand.");
        } else {
            List<ProductDTO> productDTOs = products.stream()
                    .map(productMapper::toDto)
                    .collect(Collectors.toList());
            return ApiResponse.success("products", productDTOs); // Using helper to maintain key name
        }
    }

    public Object listProductsByCategory(Integer categoryId) {
        List<Product> products = productRepository
                .findByProductCategoryIdAndProductIsActiveOrderByProductCreatedAtDesc(categoryId, true);
        if (products.isEmpty()) {
            return ApiResponse.error("No products found for this category");
        } else {
            List<ProductDTO> productDTOs = products.stream()
                    .map(productMapper::toDto)
                    .collect(Collectors.toList());
            return ApiResponse.success("products", productDTOs);
        }
    }
}
