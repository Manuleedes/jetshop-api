package com.lidigu.controller;

import com.lidigu.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("add_update_review")
    public Object addUpdateReview(@RequestParam Long user_id,
            @RequestParam String product_id,
            @RequestParam Double rating,
            @RequestParam String title,
            @RequestParam(required = false) String comment) {
        return reviewService.addUpdateReview(user_id, product_id, rating, title, comment);
    }
}
