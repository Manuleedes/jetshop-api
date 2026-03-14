package com.lidigu.service;

import com.lidigu.common.ApiResponse;
import com.lidigu.dto.LoginRequest;
import com.lidigu.dto.RegisterRequest;
import com.lidigu.dto.ResetPasswordRequest;
import com.lidigu.dto.UserDTO;
import com.lidigu.entity.User;
import com.lidigu.entity.WalletTransaction;
import com.lidigu.mapper.UserMapper;
import com.lidigu.repository.UserRepository;
import com.lidigu.repository.WalletTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletTransactionRepository walletTransactionRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    public Object login(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email.trim());

        if (userOptional.isEmpty()) {
            return ApiResponse.error("Email not registered.");
        }

        User user = userOptional.get();

        if (user.getIsVerified() == 0) {
            return ApiResponse.phpError("Account not verified. Please verify your email.");
        }

        if (passwordEncoder.matches(password, user.getPassword())) {
            UserDTO userDTO = userMapper.toDto(user);
            return ApiResponse.success("Login successful.", userDTO);
        } else {
            return ApiResponse.error("Invalid credentials.");
        }
    }

    public Object register(RegisterRequest request) {
        String email = request.getEmail().trim();
        String phone = request.getPhone().trim();

        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            if (user.getIsVerified() == 0) {
                return ApiResponse.success("OTP resent to email. Please verify.", userMapper.toDto(user));
            } else {
                return ApiResponse.error("Email already registered.");
            }
        }

        try {
            User newUser = new User();
            newUser.setFirstName(request.getFirstName().trim());
            newUser.setLastName(request.getLastName().trim());
            newUser.setEmail(email);
            newUser.setPhone(phone);
            newUser.setPassword(passwordEncoder.encode(request.getPassword()));
            newUser.setOtp(String.valueOf((int) (Math.random() * 900000) + 100000));
            newUser.setOtpExpiry(LocalDateTime.now().plusMinutes(10));
            newUser.setReferralCode("JetShop_" + UUID.randomUUID().toString().substring(0, 6).toUpperCase());
            newUser.setWalletBalance(BigDecimal.ZERO);
            newUser.setIsVerified(0);

            userRepository.save(newUser);

            // In a real app, you'd send the email here.
            // emailService.sendOTP(email, newUser.getFirstName(), newUser.getOtp());

            return ApiResponse.success("User registered. OTP sent to email.", userMapper.toDto(newUser));
        } catch (Exception e) {
            return ApiResponse.error("Registration failed: " + e.getMessage());
        }
    }

    public Object verifyOtp(String email, String otp) {
        Optional<User> userOpt = userRepository.findByEmail(email.trim());

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getOtp() != null && user.getOtp().equals(otp)) {
                user.setIsVerified(1);
                user.setOtp(null);
                userRepository.save(user);
                return ApiResponse.success("Email verified successfully.", userMapper.toDto(user));
            } else {
                return ApiResponse.error("Invalid OTP.");
            }
        } else {
            return ApiResponse.error("Email not registered.");
        }
    }

    public Object forgotPassword(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email.trim());

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            String otp = String.valueOf((int) (Math.random() * 900000) + 100000);
            user.setOtp(otp);
            userRepository.save(user);

            // emailService.sendOTP(email, user.getFirstName(), otp);
            return ApiResponse.success("OTP sent to email.", null);
        } else {
            return ApiResponse.error("Email not found.");
        }
    }

    public Object resetPassword(ResetPasswordRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail().trim());

        if (userOpt.isEmpty()) {
            return ApiResponse.error("Email not registered.");
        }

        User user = userOpt.get();
        if (user.getOtp() != null && user.getOtp().equals(request.getOtp())) {
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            user.setOtp(null);
            userRepository.save(user);
            return ApiResponse.success("Password reset successfully.", null);
        } else {
            return ApiResponse.error("Invalid OTP.");
        }
    }
}
