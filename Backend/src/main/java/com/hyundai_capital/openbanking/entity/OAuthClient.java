package com.hyundai_capital.openbanking.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "oauth_client")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OAuthClient {
    
    @Id
    @Column(name = "client_id", length = 10)
    private String clientId;  // 이용기관 ID
    
    @Column(name = "client_secret", nullable = false, length = 100)
    private String clientSecret;  // 이용기관 비밀키
    
    @Column(name = "client_name", nullable = false, length = 100)
    private String clientName;  // 이용기관 이름
    
    @Column(name = "redirect_uri", length = 200)
    private String redirectUri;  // 리다이렉트 URI
    
    @Enumerated(EnumType.STRING)
    @Column(name = "client_status", length = 10)
    @Builder.Default
    private ClientStatus clientStatus = ClientStatus.ACTIVE;  // 클라이언트 상태
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;  // 생성 시간
    
    @OneToMany(mappedBy = "oauthClient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<AccessToken> accessTokens = new ArrayList<>();
    
    public enum ClientStatus {
        ACTIVE, INACTIVE
    }
}