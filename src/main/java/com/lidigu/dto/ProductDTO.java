package com.lidigu.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductDTO {
    @JsonProperty("product_id")
    private String productId;

    @JsonProperty("product_name")
    private String productName;

    @JsonProperty("product_description")
    private String productDescription;

    @JsonProperty("product_price")
    private BigDecimal productPrice;

    @JsonProperty("product_discount_price")
    private BigDecimal productDiscountPrice;

    @JsonProperty("product_image")
    private String productImage;

    @JsonProperty("product_category_id")
    private Integer productCategoryId;

    @JsonProperty("product_quantity")
    private Integer productQuantity;

    @JsonProperty("product_rating")
    private Double productRating;

    @JsonProperty("product_total_reviews")
    private Integer productTotalReviews;

    @JsonProperty("is_featured")
    private Integer isFeatured;

    @JsonProperty("product_brand")
    private String productBrand;

    @JsonProperty("reviews")
    private List<ReviewDTO> reviews;
}
