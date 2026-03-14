package com.lidigu.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class UserDTO {
    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("profile_image")
    private String profileImage;

    @JsonProperty("wallet_balance")
    private BigDecimal walletBalance;

    @JsonProperty("is_prime")
    private Integer isPrime;

    @JsonProperty("fcm_token")
    private String fcmToken;
}
