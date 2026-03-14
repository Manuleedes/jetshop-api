package com.lidigu.mapper;

import com.lidigu.dto.ReviewDTO;
import com.lidigu.entity.Review;
import com.lidigu.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class ReviewMapper {

    @Autowired
    private UserRepository userRepository;

    public ReviewDTO toDto(Review review) {
        if (review == null)
            return null;
        ReviewDTO dto = new ReviewDTO();
        dto.setReviewId(review.getReviewId());
        dto.setUserId(review.getUserId());
        dto.setProductId(review.getProductId());
        dto.setRating(review.getRating());
        dto.setTitle(review.getTitle());
        dto.setComment(review.getComment());
        dto.setCreatedAt(review.getCreatedAt() != null ? review.getCreatedAt().toString() : null);

        // Fetch user name for DTO
        if (review.getUserId() != null) {
            userRepository.findById(review.getUserId())
                    .ifPresent(user -> dto.setUserName(user.getFirstName() + " " + user.getLastName()));
        }

        return dto;
    }
}
