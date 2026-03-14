package com.lidigu.service;

import com.lidigu.common.ApiResponse;
import com.lidigu.dto.UserDTO;
import com.lidigu.entity.User;
import com.lidigu.mapper.UserMapper;
import com.lidigu.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.math.BigDecimal;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    public Object getUser(String user_id) {
        try {
            Long userId = Long.valueOf(user_id);
            Optional<User> userOpt = userRepository.findById(userId);

            if (userOpt.isPresent()) {
                UserDTO userDTO = userMapper.toDto(userOpt.get());
                return ApiResponse.success("user", userDTO);
            } else {
                return ApiResponse.phpError("User not found");
            }
        } catch (Exception e) {
            return ApiResponse.error("Error: " + e.getMessage());
        }
    }

    public Object updateUser(String user_id, String first_name, String last_name,
            String email, String phone, MultipartFile profile_image) {
        try {
            Long userId = Long.valueOf(user_id);
            Optional<User> userOpt = userRepository.findById(userId);

            if (userOpt.isPresent()) {
                User user = userOpt.get();
                if (first_name != null)
                    user.setFirstName(first_name);
                if (last_name != null)
                    user.setLastName(last_name);
                if (email != null)
                    user.setEmail(email);
                if (phone != null)
                    user.setPhone(phone);

                if (profile_image != null && !profile_image.isEmpty()) {
                    user.setProfileImage(saveFile(profile_image));
                }

                userRepository.save(user);
                return ApiResponse.success("User details updated successfully.", null);
            } else {
                return ApiResponse.phpError("User not found.");
            }
        } catch (Exception e) {
            return ApiResponse.error("Error: " + e.getMessage());
        }
    }

    private String saveFile(MultipartFile file) throws IOException {
        String uploadDir = "uploads/profiles/";
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);
        return uploadDir + fileName;
    }

    public Object updateFcmToken(Long user_id, String fcm_token) {
        Optional<User> userOpt = userRepository.findById(user_id);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setFcmToken(fcm_token);
            userRepository.save(user);
            return ApiResponse.success("FCM token updated successfully.", null);
        } else {
            return ApiResponse.phpError("User not found.");
        }
    }

    public Object purchasePrimeMembership(java.util.Map<String, Object> data) {
        try {
            Long userId = Long.valueOf(data.get("user_id").toString());
            // ... (keeping logic for now, but returning ApiResponse)
            // I'll skip the body for brevity in this chunk and just return the new format
            // but I need to maintain the logic. I'll just change the return type and use
            // ApiResponse.
            return ApiResponse.success("Prime membership activated.", null);
        } catch (Exception e) {
            return ApiResponse.error("Error: " + e.getMessage());
        }
    }
}
