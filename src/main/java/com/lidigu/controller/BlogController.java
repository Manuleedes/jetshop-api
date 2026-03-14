package com.lidigu.controller;

import com.lidigu.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @GetMapping("get_blogs.php")
    public Map<String, Object> getBlogs() {
        return blogService.getBlogs();
    }
}
