package com.hyundai_capital.openbanking.repository;

import com.hyundai_capital.openbanking.entity.LoanContract;
import com.hyundai_capital.openbanking.entity.LoanRepaymentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepaymentInfoRepository extends JpaRepository<LoanRepaymentInfo, String> {

    Optional<LoanRepaymentInfo> findByLoanId(String loanId);
    
    Optional<LoanRepaymentInfo> findByLoanContract(LoanContract loanContract);
    
    List<LoanRepaymentInfo> findByRepayDate(String repayDate);
    
    List<LoanRepaymentInfo> findByNextRepayDate(String nextRepayDate);
    
    boolean existsByLoanId(String loanId);
    
    void deleteByLoanId(String loanId);
}