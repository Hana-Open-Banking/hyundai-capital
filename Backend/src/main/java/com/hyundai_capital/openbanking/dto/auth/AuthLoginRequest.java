package com.hyundai_capital.openbanking.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthLoginRequest {
    
    @JsonProperty("userEmail")
    private String userEmail;      // 사용자 이메일
    
    @JsonProperty("password")
    private String password;       // 비밀번호
}