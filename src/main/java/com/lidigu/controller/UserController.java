package com.lidigu.controller;

import com.lidigu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

@RestController
@RequestMapping("/")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("get_user.php")
    public Object getUser(@RequestParam String user_id) {
        return userService.getUser(user_id);
    }

    @PostMapping("update_user.php")
    public Object updateUser(@RequestParam("user_id") String user_id,
            @RequestParam(value = "first_name", required = false) String first_name,
            @RequestParam(value = "last_name", required = false) String last_name,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "profile_image", required = false) MultipartFile profile_image) {
        return userService.updateUser(user_id, first_name, last_name, email, phone, profile_image);
    }

    @PostMapping("update_fcm_token.php")
    public Object updateFcmToken(@RequestParam Long user_id,
            @RequestParam String fcm_token) {
        return userService.updateFcmToken(user_id, fcm_token);
    }

    @PostMapping("purchase_prime_membership.php")
    public Object purchasePrimeMembership(@RequestBody Map<String, Object> data) {
        return userService.purchasePrimeMembership(data);
    }
}
