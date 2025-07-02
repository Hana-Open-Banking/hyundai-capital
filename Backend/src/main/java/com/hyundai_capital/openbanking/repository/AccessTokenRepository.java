package com.hyundai_capital.openbanking.repository;

import com.hyundai_capital.openbanking.entity.AccessToken;
import com.hyundai_capital.openbanking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccessTokenRepository extends JpaRepository<AccessToken, Long> {
    
    Optional<AccessToken> findByAccessToken(String accessToken);
    
    Optional<AccessToken> findByRefreshToken(String refreshToken);
    
    List<AccessToken> findByUser(User user);
    
    List<AccessToken> findByUserAndStatus(User user, AccessToken.TokenStatus status);
    
    List<AccessToken> findByClientId(String clientId);
    
    boolean existsByAccessToken(String accessToken);
} 