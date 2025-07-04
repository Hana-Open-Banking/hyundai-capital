package com.hyundai_capital.openbanking.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @Column(name = "user_seq_no", length = 10)
    private String userSeqNo;  // 사용자일련번호 (시스템 고유 ID)

    @Column(name = "user_ci", unique = true, nullable = false, length = 100)
    private String userCi;  // CI (본인확인기관에서 발급한 고유 식별자)

    @Column(name = "user_name", nullable = false, length = 20)
    private String userName;  // 이름

    @Column(name = "user_reg_num", unique = true, nullable = false, length = 50)
    private String userRegNum;  // 주민등록번호 (해싱된 값)

    @Column(name = "gender", length = 6)
    private String gender;  // 성별 (M/F)

    @Column(name = "password", nullable = false, length = 100)
    private String password;  // 비밀번호 (암호화 저장)

    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;  // 연락처

    @Column(name = "user_email", unique = true, nullable = false, length = 100)
    private String userEmail;  // 이메일

    @Column(name = "address", length = 100)
    private String address;  // 주소

    @Column(name = "user_di", nullable = false, length = 100)
    private String userDi;  // 중복가입확인정보 (DI)

    @Enumerated(EnumType.STRING)
    @Column(name = "user_status", length = 10)
    @Builder.Default
    private UserStatus userStatus = UserStatus.ACTIVE;  // 상태

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;  // 생성일시

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;  // 수정일시

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<LoanContract> loanContracts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<AccessToken> accessTokens = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ApiResponseLog> apiResponseLogs = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private LoanInquiryContext loanInquiryContext;

    public enum UserStatus {
        ACTIVE, INACTIVE, SUSPENDED
    }
}
