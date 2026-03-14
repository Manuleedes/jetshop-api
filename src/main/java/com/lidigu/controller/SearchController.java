package com.lidigu.controller;

import com.lidigu.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("search")
    public Map<String, Object> search(@RequestParam(name = "product_name", defaultValue = "") String productName,
            @RequestParam(name = "rating", required = false) Double rating) {
        return searchService.search(productName, rating);
    }
}
