package com.hyundai_capital.openbanking.repository;

import com.hyundai_capital.openbanking.entity.AccessToken;
import com.hyundai_capital.openbanking.entity.OAuthClient;
import com.hyundai_capital.openbanking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccessTokenRepository extends JpaRepository<AccessToken, String> {

    Optional<AccessToken> findByAccessTokenId(String accessTokenId);

    Optional<AccessToken> findByRefreshToken(String refreshToken);

    List<AccessToken> findByUser(User user);

    List<AccessToken> findByUserAndTokenStatus(User user, AccessToken.TokenStatus tokenStatus);

    List<AccessToken> findByOauthClient(OAuthClient oauthClient);

    List<AccessToken> findByOauthClientAndTokenStatus(OAuthClient oauthClient, AccessToken.TokenStatus tokenStatus);

    boolean existsByAccessTokenId(String accessTokenId);
}
