package com.hyundai_capital.openbanking.repository;

import com.hyundai_capital.openbanking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUserSeqNo(String userSeqNo);

    Optional<User> findByUserEmail(String userEmail);

    Optional<User> findByUserCi(String userCi);

    Optional<User> findByUserDi(String userDi);

    boolean existsByUserSeqNo(String userSeqNo);

    boolean existsByUserEmail(String userEmail);

    boolean existsByUserCi(String userCi);

    boolean existsByUserDi(String userDi);
}
