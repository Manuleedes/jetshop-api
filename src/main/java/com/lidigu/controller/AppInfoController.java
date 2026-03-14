package com.lidigu.controller;

import com.lidigu.service.AppInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/")
public class AppInfoController {

    @Autowired
    private AppInfoService appInfoService;

    @GetMapping("get_app_info")
    public Map<String, Object> getAppInfo() {
        return appInfoService.getAppInfo();
    }
}
