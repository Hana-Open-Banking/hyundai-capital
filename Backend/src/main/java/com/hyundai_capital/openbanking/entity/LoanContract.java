package com.hyundai_capital.openbanking.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "loan_contract")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanContract {

    @Id
    @Column(name = "loan_id", length = 20)
    private String loanId;  // 대출계약 ID (내부 시스템 식별자)

    @Column(name = "loan_account_num", unique = true, nullable = false, length = 20)
    private String loanAccountNum;  // 대출계좌번호

    @Column(name = "loan_product_name", nullable = false, length = 100)
    private String loanProductName;  // 대출상품명

    @Column(name = "loan_amount", nullable = false, precision = 15, scale = 0)
    private BigDecimal loanAmount;  // 대출금액

    @Column(name = "remaining_amount", nullable = false, precision = 15, scale = 0)
    private BigDecimal remainingAmount;  // 잔액

    @Column(name = "interest_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal interestRate;  // 금리

    @Column(name = "contract_date", nullable = false, length = 8)
    private String contractDate;  // 계약일자 (YYYYMMDD)

    @Column(name = "maturity_date", nullable = false, length = 8)
    private String maturityDate;  // 만기일자 (YYYYMMDD)

    @Column(name = "repayment_day", nullable = false)
    private Integer repaymentDay;  // 상환일 (매월)

    @Enumerated(EnumType.STRING)
    @Column(name = "loan_status", length = 10)
    @Builder.Default
    private LoanStatus loanStatus = LoanStatus.ACTIVE;  // 계약상태 코드

    @Enumerated(EnumType.STRING)
    @Column(name = "loan_type", nullable = false, length = 20)
    private LoanType loanType;  // 대출종류

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq_no", nullable = false)
    private User user;

    @OneToMany(mappedBy = "loanContract", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<LoanTransaction> transactions = new ArrayList<>();

    @OneToOne(mappedBy = "loanContract", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private LoanRepaymentInfo loanRepaymentInfo;

    @OneToMany(mappedBy = "loanContract", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<AccountInfo> accountInfos = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;  // 생성일시

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;  // 수정일시

    public enum LoanStatus {
        ACTIVE, COMPLETED, OVERDUE, SUSPENDED
    }

    public enum LoanType {
        PERSONAL_LOAN,      // 개인신용대출
        MORTGAGE_LOAN,      // 주택담보대출
        AUTO_LOAN,          // 자동차대출
        BUSINESS_LOAN       // 사업자대출
    }
}
