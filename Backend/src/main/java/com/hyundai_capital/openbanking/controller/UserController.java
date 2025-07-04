package com.hyundai_capital.openbanking.controller;

import com.hyundai_capital.openbanking.dto.user.UserDto;
import com.hyundai_capital.openbanking.entity.AccessToken;
import com.hyundai_capital.openbanking.entity.User;
import com.hyundai_capital.openbanking.repository.AccessTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final AccessTokenRepository accessTokenRepository;

    private ResponseEntity<Map<String, Object>> createErrorResponse(String error, String description, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", error);
        response.put("error_description", description);
        return ResponseEntity.status(status).body(response);
    }

    private String extractToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring("Bearer ".length());
        }
        return null;
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        // Validate token
        String token = extractToken(authHeader);
        if (token == null) {
            return createErrorResponse("INVALID_TOKEN", "액세스 토큰이 없습니다", HttpStatus.UNAUTHORIZED);
        }

        // Get user by token
        Optional<AccessToken> tokenOpt = accessTokenRepository.findByAccessTokenId(token);

        if (tokenOpt.isEmpty()) {
            return createErrorResponse("INVALID_TOKEN", "액세스 토큰이 유효하지 않습니다", HttpStatus.UNAUTHORIZED);
        }

        AccessToken accessToken = tokenOpt.get();

        // Check if token is expired or inactive
        if (accessToken.isExpired() || accessToken.getTokenStatus() != AccessToken.TokenStatus.ACTIVE) {
            return createErrorResponse("INVALID_TOKEN", "액세스 토큰이 만료되었거나 비활성화되었습니다", HttpStatus.UNAUTHORIZED);
        }

        User user = accessToken.getUser();
        return ResponseEntity.ok(convertToDto(user));
    }

    private UserDto convertToDto(User user) {
        return UserDto.builder()
                .userSeqNo(user.getUserSeqNo())
                .userName(user.getUserName())
                .gender(user.getGender())
                .phoneNumber(user.getPhoneNumber())
                .userEmail(user.getUserEmail())
                .address(user.getAddress())
                .createdAt(user.getCreatedAt())
                .build();
    }
}