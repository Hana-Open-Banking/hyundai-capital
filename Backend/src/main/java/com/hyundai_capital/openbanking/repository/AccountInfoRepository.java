package com.hyundai_capital.openbanking.repository;

import com.hyundai_capital.openbanking.entity.AccountInfo;
import com.hyundai_capital.openbanking.entity.AccountInfoId;
import com.hyundai_capital.openbanking.entity.LoanContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountInfoRepository extends JpaRepository<AccountInfo, AccountInfoId> {

    List<AccountInfo> findByIdLoanId(String loanId);
    
    List<AccountInfo> findByLoanContract(LoanContract loanContract);
    
    Optional<AccountInfo> findByIdLoanIdAndIdAccountNum(String loanId, String accountNum);
    
    List<AccountInfo> findByIdAccountNum(String accountNum);
    
    boolean existsByIdLoanIdAndIdAccountNum(String loanId, String accountNum);
    
    void deleteByIdLoanId(String loanId);
}