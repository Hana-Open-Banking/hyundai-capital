package com.hyundai_capital.openbanking.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthLoginResponse {
    
    @JsonProperty("accessToken")
    private String accessToken;     // 액세스 토큰
    
    @JsonProperty("userSeqNo")
    private String userSeqNo;       // 사용자 일련번호
}