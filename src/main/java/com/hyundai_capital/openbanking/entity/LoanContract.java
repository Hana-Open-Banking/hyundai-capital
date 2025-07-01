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
@Table(name = "loan_contracts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanContract {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String loanAccountNum;  // 대출계좌번호
    
    @Column(nullable = false)
    private String loanProductName;  // 대출상품명
    
    @Column(nullable = false)
    private BigDecimal loanAmount;  // 대출금액
    
    @Column(nullable = false)
    private BigDecimal remainingAmount;  // 잔액
    
    @Column(nullable = false)
    private BigDecimal interestRate;  // 금리
    
    @Column(nullable = false)
    private LocalDate contractDate;  // 계약일자
    
    @Column(nullable = false)
    private LocalDate maturityDate;  // 만기일자
    
    @Column(nullable = false)
    private Integer repaymentDay;  // 상환일 (매월)
    
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private LoanStatus status = LoanStatus.ACTIVE;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoanType loanType;  // 대출종류
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @OneToMany(mappedBy = "loanContract", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<LoanTransaction> transactions = new ArrayList<>();
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
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