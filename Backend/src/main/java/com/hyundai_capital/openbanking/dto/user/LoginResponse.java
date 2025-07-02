package com.hyundai_capital.openbanking.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    
    @JsonProperty("access_token")
    private String accessToken;     // 액세스 토큰
    
    @JsonProperty("refresh_token")
    private String refreshToken;    // 리프레시 토큰
    
    @JsonProperty("token_type")
    @Builder.Default
    private String tokenType = "Bearer";  // 토큰 타입
    
    @JsonProperty("expires_in")
    private Long expiresIn;         // 만료 시간 (초)
    
    @JsonProperty("user_info")
    private UserInfo userInfo;      // 사용자 정보
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserInfo {
        
        @JsonProperty("user_id")
        private String userId;      // 사용자 ID
        
        @JsonProperty("name")
        private String name;        // 이름
        
        @JsonProperty("email")
        private String email;       // 이메일
        
        @JsonProperty("user_ci")
        private String userCi;      // 연계정보 (CI)
    }
} 