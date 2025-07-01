package com.hyundai_capital.openbanking.repository;

import com.hyundai_capital.openbanking.entity.LoanContract;
import com.hyundai_capital.openbanking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoanContractRepository extends JpaRepository<LoanContract, Long> {
    
    Optional<LoanContract> findByLoanAccountNum(String loanAccountNum);
    
    List<LoanContract> findByUser(User user);
    
    List<LoanContract> findByUserUserId(String userId);
    
    @Query("SELECT lc FROM LoanContract lc WHERE lc.user.userCi = :userCi AND lc.status = 'ACTIVE'")
    List<LoanContract> findActiveLoansByUserCi(@Param("userCi") String userCi);
    
    boolean existsByLoanAccountNum(String loanAccountNum);
} 