package com.beno.social_media_app.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotNull
    @Size(min = 3, max = 54)
    String firstname;

    @NotNull
    @Size(min = 3, max = 54)
    String lastname;

    @Email
    @NotNull
    String email;

    @Size(min = 8, max = 60)
    @NotNull
    String password;
}
