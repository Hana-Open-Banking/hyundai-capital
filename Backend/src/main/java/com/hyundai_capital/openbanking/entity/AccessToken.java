package com.hyundai_capital.openbanking.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "oauth_token")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccessToken {

    @Id
    @Column(name = "access_token_id", length = 100)
    private String accessTokenId;  // 엑세스 토큰 ID

    @Column(name = "refresh_token", nullable = false, length = 100)
    private String refreshToken;  // 리프레시 토큰

    @Column(name = "scope", nullable = false, length = 50)
    private String scope;  // 권한 범위

    @Column(name = "expires_in", nullable = false)
    private Integer expiresIn;  // 만료 시간 (초)

    @Column(name = "issued_at", nullable = false)
    private LocalDateTime issuedAt;  // 생성 시간

    @Column(name = "refreshed_at")
    private LocalDateTime refreshedAt;  // 갱신 시간

    @Enumerated(EnumType.STRING)
    @Column(name = "token_status", length = 10)
    @Builder.Default
    private TokenStatus tokenStatus = TokenStatus.ACTIVE;  // 토큰 상태

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq_no", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private OAuthClient oauthClient;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;  // 생성일시

    public enum TokenStatus {
        ACTIVE, EXPIRED, REVOKED
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(issuedAt.plusSeconds(expiresIn));
    }

    public boolean isRefreshExpired() {
        // 리프레시 토큰은 30일 후 만료
        return LocalDateTime.now().isAfter(issuedAt.plusDays(30));
    }
}
