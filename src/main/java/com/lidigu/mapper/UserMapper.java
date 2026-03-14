package com.lidigu.mapper;

import com.lidigu.dto.UserDTO;
import com.lidigu.entity.User;
import com.lidigu.repository.PrimeMembershipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class UserMapper {

    @Autowired
    private PrimeMembershipRepository primeMembershipRepository;

    public UserDTO toDto(User user) {
        if (user == null)
            return null;
        UserDTO dto = new UserDTO();
        dto.setUserId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setProfileImage(user.getProfileImage());
        dto.setWalletBalance(user.getWalletBalance());
        dto.setFcmToken(user.getFcmToken());

        // Check if user has active prime membership
        boolean isPrime = primeMembershipRepository.findByUserId(user.getId())
                .map(membership -> membership.getExpiryDate().isAfter(LocalDateTime.now()))
                .orElse(false);
        dto.setIsPrime(isPrime ? 1 : 0);

        return dto;
    }
}
