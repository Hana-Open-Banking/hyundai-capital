package com.hyundai_capital.openbanking.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    
    @JsonProperty("user_seq_no")
    private String userSeqNo;      // 사용자 일련번호
    
    @JsonProperty("user_name")
    private String userName;       // 이름
    
    @JsonProperty("gender")
    private String gender;         // 성별
    
    @JsonProperty("phone_number")
    private String phoneNumber;    // 연락처
    
    @JsonProperty("user_email")
    private String userEmail;      // 이메일
    
    @JsonProperty("address")
    private String address;        // 주소
    
    @JsonProperty("created_at")
    private LocalDateTime createdAt; // 생성일시
}