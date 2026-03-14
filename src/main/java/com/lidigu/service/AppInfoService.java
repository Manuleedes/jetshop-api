package com.lidigu.service;

import com.lidigu.entity.AppInfo;
import com.lidigu.repository.AppInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AppInfoService {

    @Autowired
    private AppInfoRepository appInfoRepository;

    public Map<String, Object> getAppInfo() {
        Map<String, Object> response = new HashMap<>();
        Optional<AppInfo> appInfoOptional = appInfoRepository.findLatest();

        if (appInfoOptional.isPresent()) {
            response.put("status", true);
            response.put("flag", "app_info_fetched");
            response.put("message", "App info fetched successfully");
            response.put("data", appInfoOptional.get());
        } else {
            response.put("status", false);
            response.put("flag", "not_found");
            response.put("message", "No app info found");
        }
        return response;
    }
}
