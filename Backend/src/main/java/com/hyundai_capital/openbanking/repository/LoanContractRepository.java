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
public interface LoanContractRepository extends JpaRepository<LoanContract, String> {

    Optional<LoanContract> findByLoanId(String loanId);

    Optional<LoanContract> findByLoanAccountNum(String loanAccountNum);

    List<LoanContract> findByUser(User user);

    List<LoanContract> findByUserUserSeqNo(String userSeqNo);

    @Query("SELECT lc FROM LoanContract lc WHERE lc.user.userSeqNo = :userSeqNo AND lc.loanStatus = 'ACTIVE'")
    List<LoanContract> findActiveLoansByUserSeqNo(@Param("userSeqNo") String userSeqNo);

    @Query("SELECT lc FROM LoanContract lc WHERE lc.user.userCi = :userCi AND lc.loanStatus = 'ACTIVE'")
    List<LoanContract> findActiveLoansByUserCi(@Param("userCi") String userCi);

    boolean existsByLoanId(String loanId);

    boolean existsByLoanAccountNum(String loanAccountNum);
}
