package com.beno.social_media_app.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.beno.social_media_app.auth.dto.LoginRequest;
import com.beno.social_media_app.auth.dto.RegisterRequest;
import com.beno.social_media_app.auth.service.AuthenticationService;
import com.beno.social_media_app.user.UserService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    final AuthenticationService authenticationService;
    final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody RegisterRequest request) throws Exception {
        return authenticationService.register(request);
    }

    // @PostMapping("/send-verification-code")
    // public ResponseEntity<?> sendVerificationCode(
    // @Valid @RequestBody SendVerificationDto sendVerificationDto) throws Exception
    // {
    // return authenticationService.sendVerificationCode(sendVerificationDto);
    // }

    // @PostMapping("/verify")
    // public ResponseEntity<?> verify(
    // HttpServletRequest request,
    // HttpServletResponse response,
    // @Valid @RequestBody VerifyRequest requestBody) {
    // return authenticationService.verify(requestBody, request, response);
    // }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(
            @RequestBody @Valid LoginRequest request) throws Exception {
        return authenticationService.login(request);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return authenticationService.refreshToken(request, response);
    }
}
