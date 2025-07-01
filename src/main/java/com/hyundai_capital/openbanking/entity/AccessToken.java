package com.hyundai_capital.openbanking.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "access_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccessToken {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String accessToken;  // 액세스 토큰
    
    @Column(nullable = false)
    private String refreshToken;  // 리프레시 토큰
    
    @Column(nullable = false)
    private String clientId;  // 클라이언트 ID (오픈뱅킹 센터에서 발급)
    
    @Column(nullable = false)
    private String scope;  // 권한 범위
    
    @Column(nullable = false)
    private LocalDateTime expiresAt;  // 만료일시
    
    @Column(nullable = false)
    private LocalDateTime refreshExpiresAt;  // 리프레시 토큰 만료일시
    
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private TokenStatus status = TokenStatus.ACTIVE;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    public enum TokenStatus {
        ACTIVE, EXPIRED, REVOKED
    }
    
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }
    
    public boolean isRefreshExpired() {
        return LocalDateTime.now().isAfter(refreshExpiresAt);
    }
} 