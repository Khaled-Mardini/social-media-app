package com.beno.social_media_app.auth.dto;

import com.beno.social_media_app.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VerifyResponse {

    String accessToken;
    String refreshToken;
    User patron;
}
