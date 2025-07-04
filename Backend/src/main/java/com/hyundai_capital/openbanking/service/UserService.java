package com.hyundai_capital.openbanking.service;

import com.hyundai_capital.openbanking.dto.auth.AuthLoginRequest;
import com.hyundai_capital.openbanking.dto.auth.AuthLoginResponse;
import com.hyundai_capital.openbanking.dto.user.LoginRequest;
import com.hyundai_capital.openbanking.dto.user.LoginResponse;
import com.hyundai_capital.openbanking.dto.user.UserRegistrationRequest;
import com.hyundai_capital.openbanking.entity.AccessToken;
import com.hyundai_capital.openbanking.entity.OAuthClient;
import com.hyundai_capital.openbanking.entity.User;
import com.hyundai_capital.openbanking.repository.AccessTokenRepository;
import com.hyundai_capital.openbanking.repository.OAuthClientRepository;
import com.hyundai_capital.openbanking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final AccessTokenRepository accessTokenRepository;
    private final OAuthClientRepository oAuthClientRepository;

    public User registerUser(UserRegistrationRequest request) {
        // 중복 확인
        if (userRepository.existsByUserSeqNo(request.getUserId())) {
            throw new RuntimeException("이미 존재하는 사용자 ID입니다.");
        }

        if (userRepository.existsByUserEmail(request.getEmail())) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }

        if (userRepository.existsByUserCi(request.getUserCi())) {
            throw new RuntimeException("이미 존재하는 CI입니다.");
        }

        // 사용자 생성
        User user = User.builder()
                .userSeqNo(request.getUserId())
                .password(hashPassword(request.getPassword())) // 간단한 해싱
                .userName(request.getName())
                .userEmail(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .userRegNum(hashCitizenNumber(request.getCitizenNumber()))
                .address(request.getAddress())
                .userCi(request.getUserCi())
                .userDi(request.getUserDi())
                .userStatus(User.UserStatus.ACTIVE)
                .build();

        return userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUserSeqNo(request.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        if (!user.getPassword().equals(hashPassword(request.getPassword()))) {
            throw new RuntimeException("비밀번호가 올바르지 않습니다.");
        }

        if (user.getUserStatus() != User.UserStatus.ACTIVE) {
            throw new RuntimeException("비활성화된 사용자입니다.");
        }

        // 간단한 토큰 생성 (실제로는 JWT를 사용해야 함)
        String accessToken = generateSimpleToken(user.getUserSeqNo());
        String refreshToken = generateSimpleToken(user.getUserSeqNo() + "_refresh");

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(86400L) // 24시간
                .userInfo(LoginResponse.UserInfo.builder()
                        .userId(user.getUserSeqNo())
                        .name(user.getUserName())
                        .email(user.getUserEmail())
                        .userCi(user.getUserCi())
                        .build())
                .build();
    }

    @Transactional(readOnly = true)
    public Optional<User> findByUserSeqNo(String userSeqNo) {
        return userRepository.findByUserSeqNo(userSeqNo);
    }

    @Transactional(readOnly = true)
    public Optional<User> findByUserCi(String userCi) {
        return userRepository.findByUserCi(userCi);
    }

    private String hashPassword(String password) {
        // 실제로는 암호화해야 함 (BCrypt 등)
        System.out.println("hash pwd" + password);
        return "HASHED_" + password;
    }

    private String hashCitizenNumber(String citizenNumber) {
        // 실제로는 암호화해야 함
        return "HASHED_" + citizenNumber.hashCode();
    }

    private String generateSimpleToken(String input) {
        // 실제로는 JWT를 사용해야 함
        return "TOKEN_" + UUID.randomUUID() + "_" + input.hashCode();
    }

    public AuthLoginResponse loginByEmail(AuthLoginRequest request) {
        User user = userRepository.findByUserEmail(request.getUserEmail())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        if (!user.getPassword().equals(hashPassword(request.getPassword()))) {
            throw new RuntimeException("비밀번호가 올바르지 않습니다.");
        }

        if (user.getUserStatus() != User.UserStatus.ACTIVE) {
            throw new RuntimeException("비활성화된 사용자입니다.");
        }

        // 간단한 토큰 생성
        String accessToken = generateSimpleToken(user.getUserSeqNo());
        String refreshToken = generateSimpleToken(user.getUserSeqNo() + "_refresh");

        // 기본 클라이언트 조회 (현대캐피탈 오픈뱅킹)
        OAuthClient client = oAuthClientRepository.findByClientId("CLIENT001")
                .orElseThrow(() -> new RuntimeException("OAuth 클라이언트를 찾을 수 없습니다."));

        // 토큰 엔티티 생성 및 저장
        AccessToken tokenEntity = AccessToken.builder()
                .accessTokenId(accessToken)
                .refreshToken(refreshToken)
                .scope("read")
                .expiresIn(3600) // 1시간
                .issuedAt(LocalDateTime.now())
                .tokenStatus(AccessToken.TokenStatus.ACTIVE)
                .user(user)
                .oauthClient(client)
                .build();

        accessTokenRepository.save(tokenEntity);
        log.info("토큰 생성 및 저장 완료: {}", accessToken);

        return AuthLoginResponse.builder()
                .accessToken(accessToken)
                .userSeqNo(user.getUserSeqNo())
                .build();
    }
}
