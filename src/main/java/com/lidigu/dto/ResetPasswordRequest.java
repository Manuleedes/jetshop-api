package com.lidigu.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ResetPasswordRequest {
    private String email;
    private String otp;

    @JsonProperty("new_password")
    private String newPassword;
}
