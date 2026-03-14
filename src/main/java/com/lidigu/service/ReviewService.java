package com.lidigu.service;

import com.lidigu.common.ApiResponse;
import com.lidigu.entity.Review;
import com.lidigu.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public Object addUpdateReview(Long user_id, String product_id, Double rating,
            String title, String comment) {

        if (rating < 1.0 || rating > 5.0) {
            return ApiResponse.phpError("Invalid rating value. It must be between 1.0 and 5.0");
        }

        Optional<Review> reviewOpt = reviewRepository.findByProductIdAndUserId(product_id, user_id);

        if (reviewOpt.isPresent()) {
            Review review = reviewOpt.get();
            review.setRating(rating);
            review.setTitle(title);
            review.setComment(comment);
            reviewRepository.save(review);
            return ApiResponse.success("Review updated successfully.", null);
        } else {
            Review review = new Review();
            review.setReviewId("REV" + UUID.randomUUID().toString().substring(0, 10).toUpperCase());
            review.setProductId(product_id);
            review.setUserId(user_id);

            review.setRating(rating);
            review.setTitle(title);
            review.setComment(comment);
            reviewRepository.save(review);
            return ApiResponse.success("Review added successfully.", null);
        }
    }
}
