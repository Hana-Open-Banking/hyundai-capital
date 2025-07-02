package com.hyundai_capital.openbanking.repository;

import com.hyundai_capital.openbanking.entity.LoanContract;
import com.hyundai_capital.openbanking.entity.LoanTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface LoanTransactionRepository extends JpaRepository<LoanTransaction, Long> {
    
    Optional<LoanTransaction> findByTransactionUniqueNo(String transactionUniqueNo);
    
    List<LoanTransaction> findByLoanContract(LoanContract loanContract);
    
    Page<LoanTransaction> findByLoanContract(LoanContract loanContract, Pageable pageable);
    
    @Query("SELECT lt FROM LoanTransaction lt WHERE lt.loanContract.loanAccountNum = :loanAccountNum " +
           "AND lt.transactionDate >= :fromDate AND lt.transactionDate <= :toDate " +
           "ORDER BY lt.transactionDate DESC")
    List<LoanTransaction> findByLoanAccountNumAndDateRange(
            @Param("loanAccountNum") String loanAccountNum,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate);
    
    @Query("SELECT lt FROM LoanTransaction lt WHERE lt.loanContract.loanAccountNum = :loanAccountNum " +
           "AND lt.transactionDate >= :fromDate AND lt.transactionDate <= :toDate " +
           "ORDER BY lt.transactionDate DESC")
    Page<LoanTransaction> findByLoanAccountNumAndDateRange(
            @Param("loanAccountNum") String loanAccountNum,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate,
            Pageable pageable);
} 