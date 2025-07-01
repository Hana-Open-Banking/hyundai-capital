package com.hyundai_capital.openbanking.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "loan_transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanTransaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String transactionUniqueNo;  // 거래고유번호
    
    @Column(nullable = false)
    private LocalDateTime transactionDate;  // 거래일시
    
    @Column(nullable = false)
    private BigDecimal transactionAmount;  // 거래금액
    
    @Column(nullable = false)
    private BigDecimal afterBalance;  // 거래후잔액
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType transactionType;  // 거래구분
    
    @Column(nullable = false)
    private String transactionSummary;  // 거래적요
    
    private String branchName;  // 점명
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_contract_id", nullable = false)
    private LoanContract loanContract;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    public enum TransactionType {
        LOAN_EXECUTION,     // 대출실행
        PRINCIPAL_PAYMENT,  // 원금상환
        INTEREST_PAYMENT,   // 이자납부
        TOTAL_PAYMENT,      // 원리금상환
        FEE_PAYMENT,        // 수수료납부
        EARLY_REPAYMENT     // 중도상환
    }
} 