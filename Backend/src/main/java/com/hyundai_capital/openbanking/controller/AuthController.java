package com.hyundai_capital.openbanking.controller;

import com.hyundai_capital.openbanking.dto.auth.AuthLoginRequest;
import com.hyundai_capital.openbanking.dto.auth.AuthLoginResponse;
import com.hyundai_capital.openbanking.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<AuthLoginResponse> login(@RequestBody AuthLoginRequest request) {
        try {
            log.info("사용자 로그인 요청: {}", request.getUserEmail());
            
            AuthLoginResponse response = userService.loginByEmail(request);
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            log.error("로그인 오류: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}