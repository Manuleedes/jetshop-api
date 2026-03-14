package com.lidigu.service;

import com.lidigu.entity.Blog;
import com.lidigu.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BlogService {

    @Autowired
    private BlogRepository blogRepository;

    public Map<String, Object> getBlogs() {
        Map<String, Object> response = new HashMap<>();
        List<Blog> blogs = blogRepository.findAllByOrderByPublishedDateDesc();

        if (blogs.isEmpty()) {
            response.put("status", "error");
            response.put("flag", "no_blogs");
            response.put("message", "No blogs found.");
        } else {
            response.put("status", "success");
            response.put("flag", "blogs_fetched");
            response.put("message", "Blogs fetched successfully.");
            response.put("data", blogs);
        }
        return response;
    }
}
