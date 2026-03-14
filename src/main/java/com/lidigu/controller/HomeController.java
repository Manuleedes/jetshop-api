package com.lidigu.controller;

import com.lidigu.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/")
public class HomeController {

    @Autowired
    private HomeService homeService;

    @GetMapping("get_slider_category_product")
    public Map<String, Object> getHomeData(@RequestParam(name = "user_id", required = false) String userId) {
        return homeService.getHomeData(userId);
    }
}
