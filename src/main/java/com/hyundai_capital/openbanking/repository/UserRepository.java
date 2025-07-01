package com.hyundai_capital.openbanking.repository;

import com.hyundai_capital.openbanking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUserId(String userId);
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findByUserCi(String userCi);
    
    Optional<User> findByUserDi(String userDi);
    
    boolean existsByUserId(String userId);
    
    boolean existsByEmail(String email);
    
    boolean existsByUserCi(String userCi);
    
    boolean existsByUserDi(String userDi);
} 