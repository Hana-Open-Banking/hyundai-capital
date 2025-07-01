package com.hyundai_capital.openbanking.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String userId;  // 사용자 ID
    
    @Column(nullable = false)
    private String password;  // 비밀번호
    
    @Column(nullable = false)
    private String name;  // 이름
    
    @Column(unique = true, nullable = false)
    private String email;  // 이메일
    
    @Column(nullable = false)
    private String phoneNumber;  // 전화번호
    
    @Column(unique = true, nullable = false)
    private String citizenNumber;  // 주민등록번호 (해싱된 값)
    
    private String address;  // 주소
    
    @Column(nullable = false)
    private String userCi;  // 연계정보 (CI)
    
    @Column(nullable = false)
    private String userDi;  // 중복가입확인정보 (DI)
    
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private UserStatus status = UserStatus.ACTIVE;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<LoanContract> loanContracts = new ArrayList<>();
    
    public enum UserStatus {
        ACTIVE, INACTIVE, SUSPENDED
    }
} 