package com.beno.social_media_app.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beno.social_media_app.auth.dto.LoginRequest;
import com.beno.social_media_app.auth.dto.LoginResponse;
import com.beno.social_media_app.auth.dto.RegisterRequest;
import com.beno.social_media_app.auth.enums.Role;
import com.beno.social_media_app.auth.enums.TokenType;
import com.beno.social_media_app.auth.model.Token;
import com.beno.social_media_app.auth.repository.TokenRepository;
import com.beno.social_media_app.user.User;
import com.beno.social_media_app.user.UserRepository;
import com.beno.social_media_app.user.UserService;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    final TokenRepository tokenRepository;
    final UserRepository userRepository;
    final UserService userService;
    final JwtService jwtService;
    final AuthenticationManager authenticationManager;
    final EmailService emailService;

    @Value("${application.security.activation-key.duration.min}")
    long activationKeyDurationMinutes;

    private final Logger log = LoggerFactory.getLogger(AuthenticationService.class);

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    ModelMapper modelMapper = new ModelMapper();

    public ResponseEntity<?> register(RegisterRequest request) throws Exception {
        Optional<User> lookupUser = userRepository.findByEmail(request.getEmail());
        if (lookupUser.isPresent()) {
            throw new Exception("Email Already exist");
        }
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        User user = modelMapper.map(request, User.class);
        user.setRole(Role.USER);
        // patron.setActivationKey(generateActivationKey());
        // patron.setActivationKeyCreation(Instant.now());
        // patron.setRole(Role.USER);
        // User savedUser = userService.create(patron).getBody().data();
        // emailService.sendVerificationEmail(savedPatron.getEmail(),
        // savedPatron.getActivationKey());
        user = userRepository.save(user);
        return ResponseEntity.ok(user);
    }

    // public ResponseEntity<?> sendVerificationCode(SendVerificationDto
    // sendVerificationDto) {
    // Optional<Patron> lookupPatron =
    // patronRepository.findByEmail(sendVerificationDto.getEmail());
    // if (lookupPatron.isEmpty()) {
    // throw new CommonExceptions.ResourceNotFoundException("Email does not exist,
    // please register first");
    // }
    // Patron patron = lookupPatron.get();
    // patron.setActivationKey(generateActivationKey());
    // patron.setActivationKeyCreation(Instant.now());
    // Patron savedPatron = patronService.create(patron).getBody().data();
    // emailService.sendVerificationEmail(savedPatron.getEmail(),
    // savedPatron.getActivationKey());
    // return ResponseDto.response(null, HttpStatus.OK, "Verification code sent
    // successfully");
    // }

    // @Transactional
    // public ResponseEntity<ResponseDto<VerifyResponse>> verify(
    // VerifyRequest verifyRequest,
    // HttpServletRequest request,
    // HttpServletResponse response) {
    // Optional<Patron> lookupPatron =
    // patronRepository.findByEmail(verifyRequest.getEmail());
    // if (lookupPatron.isEmpty()) {
    // throw new CommonExceptions.ResourceNotFoundException("Email does not exist");
    // }
    // Patron patron = lookupPatron.get();
    // if (!Objects.equals(verifyRequest.getCode(), patron.getActivationKey())) {
    // log.info("invalid activation code attempt: session-" + request.getSession()
    // + ", email-" + patron.getEmail());
    // throw new CommonExceptions.UnauthorizedException("Invalid activation code");
    // }
    // Duration timeDuration = Duration.between(patron.getActivationKeyCreation(),
    // Instant.now());
    // if (timeDuration.compareTo(Duration.ofMinutes(activationKeyDurationMinutes))
    // > 0) {
    // throw new CommonExceptions.UnauthorizedException("Expired activation code");
    // }
    // patron.setActivated(true);
    // patronRepository.save(patron);
    // var jwtToken = jwtService.generateToken(patron);
    // var newRefreshToken = jwtService.generateRefreshToken(patron);
    // savePatronToken(patron, jwtToken);
    // var data = VerifyResponse.builder()
    // .patron(patron)
    // .accessToken(jwtToken)
    // .refreshToken(newRefreshToken)
    // .build();
    // return ResponseDto.response(data, HttpStatus.OK, "Account verified
    // successfully");
    // }

    @Transactional
    public ResponseEntity<?> login(LoginRequest request) throws Exception {
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new Exception("email does not exist"));
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllPatronTokens(user);
        savePatronToken(user, jwtToken);
        var data = LoginResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
        return ResponseEntity.ok(data);
    }

    void savePatronToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    String generateActivationKey() {
        return (int) Math.floor(Math.random() * (99998 - 10000 + 1) + 10000) + "";
    }

    void revokeAllPatronTokens(User user) {
        log.info("revoked all tokens for patron " + user.getEmail());
        var validPatronTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validPatronTokens.isEmpty())
            return;
        validPatronTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validPatronTokens);
    }

    User getPatronFromRefreshToken(String refreshToken) throws Exception {
        String userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            String message = userEmail + "does not exist";
            var user = this.userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new Exception(message));
            if (jwtService.isTokenValid(refreshToken, user)) {
                return user;
            }
        }
        throw new Exception("Invalid token");
    }

    String getRefreshTokenFromAuthHeader(HttpServletRequest request) throws Exception {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new Exception("invalid authorization header");
        }
        refreshToken = authHeader.substring(7);
        return refreshToken;
    }

    public ResponseEntity<?> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String refreshToken = getRefreshTokenFromAuthHeader(request);
        User user = getPatronFromRefreshToken(refreshToken);
        var accessToken = jwtService.generateToken(user);
        revokeAllPatronTokens(user);
        savePatronToken(user, accessToken);
        var authResponse = LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        return ResponseEntity.ok(authResponse);
    }
}
