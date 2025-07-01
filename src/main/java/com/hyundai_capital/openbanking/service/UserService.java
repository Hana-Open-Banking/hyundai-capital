package com.hyundai_capital.openbanking.service;

import com.hyundai_capital.openbanking.dto.user.LoginRequest;
import com.hyundai_capital.openbanking.dto.user.LoginResponse;
import com.hyundai_capital.openbanking.dto.user.UserRegistrationRequest;
import com.hyundai_capital.openbanking.entity.User;
import com.hyundai_capital.openbanking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {
    
    private final UserRepository userRepository;
    
    public User registerUser(UserRegistrationRequest request) {
        // 중복 확인
        if (userRepository.existsByUserId(request.getUserId())) {
            throw new RuntimeException("이미 존재하는 사용자 ID입니다.");
        }
        
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }
        
        if (userRepository.existsByUserCi(request.getUserCi())) {
            throw new RuntimeException("이미 존재하는 CI입니다.");
        }
        
        // 사용자 생성
        User user = User.builder()
                .userId(request.getUserId())
                .password(hashPassword(request.getPassword())) // 간단한 해싱
                .name(request.getName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .citizenNumber(hashCitizenNumber(request.getCitizenNumber()))
                .address(request.getAddress())
                .userCi(request.getUserCi())
                .userDi(request.getUserDi())
                .status(User.UserStatus.ACTIVE)
                .build();
        
        return userRepository.save(user);
    }
    
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        
        if (!user.getPassword().equals(hashPassword(request.getPassword()))) {
            throw new RuntimeException("비밀번호가 올바르지 않습니다.");
        }
        
        if (user.getStatus() != User.UserStatus.ACTIVE) {
            throw new RuntimeException("비활성화된 사용자입니다.");
        }
        
        // 간단한 토큰 생성 (실제로는 JWT를 사용해야 함)
        String accessToken = generateSimpleToken(user.getUserId());
        String refreshToken = generateSimpleToken(user.getUserId() + "_refresh");
        
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(86400L) // 24시간
                .userInfo(LoginResponse.UserInfo.builder()
                        .userId(user.getUserId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .userCi(user.getUserCi())
                        .build())
                .build();
    }
    
    @Transactional(readOnly = true)
    public Optional<User> findByUserId(String userId) {
        return userRepository.findByUserId(userId);
    }
    
    @Transactional(readOnly = true)
    public Optional<User> findByUserCi(String userCi) {
        return userRepository.findByUserCi(userCi);
    }
    
    private String hashPassword(String password) {
        // 실제로는 암호화해야 함 (BCrypt 등)
        return "HASHED_" + password.hashCode();
    }
    
    private String hashCitizenNumber(String citizenNumber) {
        // 실제로는 암호화해야 함
        return "HASHED_" + citizenNumber.hashCode();
    }
    
    private String generateSimpleToken(String input) {
        // 실제로는 JWT를 사용해야 함
        return "TOKEN_" + UUID.randomUUID().toString() + "_" + input.hashCode();
    }
} 