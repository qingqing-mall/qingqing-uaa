package com.liaoyb.qingqing.uaa.service.dto;

import lombok.Data;

/**
 * A DTO representing a password change required data - current and new password.
 */
@Data
public class PasswordChangeDTO {
    private String currentPassword;
    private String newPassword;

    public PasswordChangeDTO() {
        // Empty constructor needed for Jackson.
    }

    public PasswordChangeDTO(String currentPassword, String newPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }
}
