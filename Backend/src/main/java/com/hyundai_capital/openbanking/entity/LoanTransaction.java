package com.hyundai_capital.openbanking.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "loan_transaction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanTransaction {

    @Id
    @Column(name = "transaction_id", length = 20)
    private String transactionId;  // 거래 ID

    @Column(name = "transaction_unique_no", unique = true, nullable = false, length = 20)
    private String transactionUniqueNo;  // 거래고유번호

    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;  // 거래일시

    @Column(name = "transaction_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal transactionAmount;  // 거래금액

    @Column(name = "after_balance", nullable = false, precision = 15, scale = 2)
    private BigDecimal afterBalance;  // 거래후잔액

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false, length = 20)
    private TransactionType transactionType;  // 거래구분

    @Column(name = "transaction_summary", nullable = false, length = 100)
    private String transactionSummary;  // 거래적요

    @Column(name = "branch_name", length = 50)
    private String branchName;  // 점명

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_id", nullable = false)
    private LoanContract loanContract;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;  // 생성일시

    public enum TransactionType {
        LOAN_EXECUTION,     // 대출실행
        PRINCIPAL_PAYMENT,  // 원금상환
        INTEREST_PAYMENT,   // 이자납부
        TOTAL_PAYMENT,      // 원리금상환
        FEE_PAYMENT,        // 수수료납부
        EARLY_REPAYMENT     // 중도상환
    }
}
