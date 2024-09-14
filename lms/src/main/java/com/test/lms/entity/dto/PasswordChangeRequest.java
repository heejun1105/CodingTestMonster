package com.test.lms.entity.dto;

import lombok.Data;

@Data
public class PasswordChangeRequest {
    private String username;
    private String newPassword;
    private String confirmPassword;
}
