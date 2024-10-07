package com.beno.social_media_app.auth.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChangePasswordRequest {

    @NotNull
    String currentPassword;

    @NotNull
    String newPassword;

    @NotNull
    String confirmationPassword;
}
