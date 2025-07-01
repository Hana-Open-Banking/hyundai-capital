package com.hyundai_capital.openbanking.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegistrationRequest {
    
    @JsonProperty("user_id")
    private String userId;          // 사용자 ID
    
    @JsonProperty("password")
    private String password;        // 비밀번호
    
    @JsonProperty("name")
    private String name;            // 이름
    
    @JsonProperty("email")
    private String email;           // 이메일
    
    @JsonProperty("phone_number")
    private String phoneNumber;     // 전화번호
    
    @JsonProperty("citizen_number")
    private String citizenNumber;   // 주민등록번호
    
    @JsonProperty("address")
    private String address;         // 주소
    
    @JsonProperty("user_ci")
    private String userCi;          // 연계정보 (CI)
    
    @JsonProperty("user_di")
    private String userDi;          // 중복가입확인정보 (DI)
} 