package com.hyundai_capital.openbanking.repository;

import com.hyundai_capital.openbanking.entity.OAuthClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface OAuthClientRepository extends JpaRepository<OAuthClient, String> {
    
    Optional<OAuthClient> findByClientId(String clientId);
    
    Optional<OAuthClient> findByClientIdAndClientSecret(String clientId, String clientSecret);
    
    List<OAuthClient> findByClientStatus(OAuthClient.ClientStatus status);
    
    boolean existsByClientId(String clientId);
    
    boolean existsByClientName(String clientName);
}