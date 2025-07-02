package com.hyundai_capital.openbanking.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {
    
    @JsonProperty("user_id")
    private String userId;      // 사용자 ID
    
    @JsonProperty("password")
    private String password;    // 비밀번호
} 