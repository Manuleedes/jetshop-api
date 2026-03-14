package com.lidigu.controller;

import com.lidigu.dto.LoginRequest;
import com.lidigu.dto.RegisterRequest;
import com.lidigu.dto.ResetPasswordRequest;
import com.lidigu.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("login.php")
    public Object login(@RequestParam String email, @RequestParam String password) {
        return authService.login(email, password);
    }

    @PostMapping("register.php")
    public Object register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("verify_otp.php")
    public Object verifyOtp(@RequestParam String email, @RequestParam String otp) {
        return authService.verifyOtp(email, otp);
    }

    @PostMapping("forgot_password.php")
    public Object forgotPassword(@RequestParam String email) {
        return authService.forgotPassword(email);
    }

    @PostMapping("reset_password.php")
    public Object resetPassword(@RequestBody ResetPasswordRequest request) {
        return authService.resetPassword(request);
    }
}
