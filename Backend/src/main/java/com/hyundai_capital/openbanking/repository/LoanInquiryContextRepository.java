package com.hyundai_capital.openbanking.repository;

import com.hyundai_capital.openbanking.entity.LoanInquiryContext;
import com.hyundai_capital.openbanking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoanInquiryContextRepository extends JpaRepository<LoanInquiryContext, String> {

    Optional<LoanInquiryContext> findByUserSeqNo(String userSeqNo);
    
    Optional<LoanInquiryContext> findByUser(User user);
    
    boolean existsByUserSeqNo(String userSeqNo);
    
    void deleteByUserSeqNo(String userSeqNo);
}